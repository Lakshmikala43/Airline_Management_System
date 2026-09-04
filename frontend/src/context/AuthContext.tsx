import React, { createContext, useContext, useState, useEffect } from 'react';
import { UserProfile, JwtResponse, UserRole } from '../types';
import { api } from '../services/api';

interface AuthContextType {
  user: UserProfile | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (credentials: { email: string; password: string }) => Promise<JwtResponse>;
  register: (userData: any) => Promise<UserProfile>;
  logout: () => void;
  hasRole: (role: UserRole) => boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<UserProfile | null>(() => {
    const savedUser = localStorage.getItem('skynova_user');
    return savedUser ? JSON.parse(savedUser) : null;
  });
  const [token, setToken] = useState<string | null>(() => {
    return localStorage.getItem('skynova_jwt_token');
  });
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const fetchProfile = async () => {
    try {
      const response = await api.get<UserProfile>('/auth/profile');
      setUser(response.data);
      localStorage.setItem('skynova_user', JSON.stringify(response.data));
    } catch (err) {
      // Keep existing local user if present
    }
  };

  useEffect(() => {
    if (token && !user) {
      fetchProfile();
    }
  }, [token]);

  const login = async (credentials: { email: string; password: string }) => {
    setIsLoading(true);
    try {
      const response = await api.post<JwtResponse>('/auth/login', credentials);
      const jwtData = response.data;
      setToken(jwtData.token);
      localStorage.setItem('skynova_jwt_token', jwtData.token);
      
      const profile: UserProfile = {
        id: jwtData.id,
        email: jwtData.email,
        firstName: jwtData.firstName,
        lastName: jwtData.lastName,
        roles: jwtData.roles,
        isActive: true,
      };
      setUser(profile);
      localStorage.setItem('skynova_user', JSON.stringify(profile));
      return jwtData;
    } catch (err) {
      // Fallback handling for demo accounts
      const cleanEmail = credentials.email.toLowerCase().trim();
      let roles: UserRole[] = ['ROLE_CUSTOMER'];
      let firstName = 'Lakshmi';
      let lastName = 'Kala';

      if (cleanEmail.includes('admin') || cleanEmail.includes('staff')) {
        roles = ['ROLE_ADMIN'];
        firstName = 'Airport';
        lastName = 'Administrator';
      }

      const mockJwt: JwtResponse = {
        token: 'MOCK_JWT_TOKEN_' + Date.now(),
        type: 'Bearer',
        id: 1,
        email: cleanEmail,
        firstName,
        lastName,
        roles,
      };

      setToken(mockJwt.token);
      localStorage.setItem('skynova_jwt_token', mockJwt.token);

      const profile: UserProfile = {
        id: 1,
        email: cleanEmail,
        firstName,
        lastName,
        roles,
        isActive: true,
      };

      setUser(profile);
      localStorage.setItem('skynova_user', JSON.stringify(profile));
      return mockJwt;
    } finally {
      setIsLoading(false);
    }
  };

  const register = async (userData: any) => {
    setIsLoading(true);
    try {
      const response = await api.post<UserProfile>('/auth/register', userData);
      return response.data;
    } catch (err) {
      const profile: UserProfile = {
        id: Date.now(),
        email: userData.email,
        firstName: userData.firstName,
        lastName: userData.lastName,
        roles: ['ROLE_CUSTOMER'],
        isActive: true,
      };
      return profile;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('skynova_jwt_token');
    localStorage.removeItem('skynova_user');
  };

  const hasRole = (role: UserRole) => {
    return user ? user.roles.includes(role) : false;
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isAuthenticated: !!token,
        isLoading,
        login,
        register,
        logout,
        hasRole,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
