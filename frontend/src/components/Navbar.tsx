import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Plane, User, LogOut, Ticket, Search, ShieldCheck, LayoutDashboard } from 'lucide-react';

export const Navbar: React.FC = () => {
  const { user, isAuthenticated, logout, hasRole } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <header className="bg-white text-slate-900 shadow-sm sticky top-0 z-50 border-b border-slate-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          
          {/* Brand Logo */}
          <Link to="/" className="flex items-center space-x-3 group">
            <div className="bg-gradient-to-tr from-orange-600 to-amber-500 p-2.5 rounded-xl text-white group-hover:scale-105 transition-transform shadow-md shadow-orange-500/20">
              <Plane className="h-6 w-6 transform -rotate-45" />
            </div>
            <div>
              <span className="text-xl font-black tracking-tight text-slate-900">
                SkyNova <span className="text-orange-500">Airways</span>
              </span>
              <span className="text-[10px] uppercase tracking-widest text-orange-600 block -mt-1 font-bold">
                Smart Aviation
              </span>
            </div>
          </Link>

          {/* Navigation Links */}
          <nav className="hidden md:flex items-center space-x-6">
            <Link to="/search" className="text-slate-700 hover:text-orange-500 flex items-center space-x-1.5 font-bold text-sm transition-colors">
              <Search className="h-4 w-4 text-orange-500" />
              <span>Search Flights</span>
            </Link>
            <Link to="/lookup" className="text-slate-700 hover:text-orange-500 flex items-center space-x-1.5 font-bold text-sm transition-colors">
              <Ticket className="h-4 w-4 text-orange-500" />
              <span>Manage Booking</span>
            </Link>
            <Link to="/verify-ticket" className="text-slate-700 hover:text-orange-500 flex items-center space-x-1.5 font-bold text-sm transition-colors">
              <ShieldCheck className="h-4 w-4 text-orange-500" />
              <span>Verify Ticket</span>
            </Link>
            {isAuthenticated && hasRole('ROLE_ADMIN') && (
              <Link to="/admin" className="text-orange-600 hover:text-orange-700 flex items-center space-x-1.5 font-black text-sm transition-colors bg-orange-50 px-3 py-1.5 rounded-lg border border-orange-200">
                <LayoutDashboard className="h-4 w-4 text-orange-500" />
                <span>Admin Portal</span>
              </Link>
            )}
          </nav>

          {/* Auth Actions */}
          <div className="flex items-center space-x-4">
            {isAuthenticated ? (
              <div className="flex items-center space-x-3">
                <Link
                  to="/dashboard"
                  className="flex items-center space-x-2 bg-orange-50 hover:bg-orange-100 text-orange-900 px-3.5 py-2 rounded-xl border border-orange-200 text-sm font-extrabold transition-all"
                >
                  <User className="h-4 w-4 text-orange-600" />
                  <span className="max-w-[120px] truncate">{user?.firstName} {user?.lastName}</span>
                </Link>
                <button
                  onClick={handleLogout}
                  className="p-2 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-lg transition-colors"
                  title="Sign Out"
                >
                  <LogOut className="h-5 w-5" />
                </button>
              </div>
            ) : (
              <div className="flex items-center space-x-3">
                <Link
                  to="/login"
                  className="text-slate-700 hover:text-orange-600 text-sm font-bold px-3 py-2 transition-colors"
                >
                  Sign In
                </Link>
                <Link
                  to="/register"
                  className="bg-orange-500 hover:bg-orange-600 text-white text-sm font-extrabold px-4 py-2.5 rounded-xl shadow-md shadow-orange-500/20 transition-all hover:scale-105"
                >
                  Register
                </Link>
              </div>
            )}
          </div>

        </div>
      </div>
    </header>
  );
};
