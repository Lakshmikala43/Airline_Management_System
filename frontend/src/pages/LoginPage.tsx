import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Plane, Lock, Mail, AlertCircle, Sparkles, User, ShieldCheck } from 'lucide-react';

export const LoginPage: React.FC = () => {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [email, setEmail] = useState('customer@skynova.demo');
  const [password, setPassword] = useState('Password123!');
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const performLogin = async (loginEmail: string, loginPass: string) => {
    setError('');
    setIsSubmitting(true);
    try {
      const res = await login({ email: loginEmail, password: loginPass });
      if (res.roles.includes('ROLE_ADMIN')) {
        navigate('/admin');
      } else {
        navigate('/dashboard');
      }
    } catch (err: any) {
      setError(err.response?.data?.message || 'Invalid email or password');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    performLogin(email, password);
  };

  const autofillAndLogin = (roleEmail: string) => {
    setEmail(roleEmail);
    setPassword('Password123!');
    performLogin(roleEmail, 'Password123!');
  };

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-4 sm:px-6 lg:px-8 py-12">
      <div className="max-w-md w-full bg-white rounded-3xl shadow-2xl border border-slate-200 p-8 space-y-6">
        
        <div className="text-center space-y-2">
          <div className="w-12 h-12 bg-orange-500 rounded-2xl text-white flex items-center justify-center mx-auto shadow-lg shadow-orange-500/30">
            <Plane className="h-6 w-6 transform -rotate-45" />
          </div>
          <h2 className="text-2xl font-black text-slate-900">Sign In to SkyNova <span className="text-orange-500">Airways</span></h2>
          <p className="text-xs text-slate-500 font-medium">Access Passenger Booking Portal or Airport Staff Administration</p>
        </div>

        {/* 2 Major Sides One-Click Sign In Banner */}
        <div className="bg-orange-50/80 p-4 rounded-2xl border border-orange-200 text-center space-y-3 shadow-inner">
          <span className="text-[10px] font-extrabold uppercase tracking-wider text-orange-700 block flex items-center justify-center gap-1">
            <Sparkles className="h-3.5 w-3.5 text-orange-500" /> Viva One-Click Portal Login
          </span>
          <div className="grid grid-cols-2 gap-3 pt-1">
            <button
              type="button"
              onClick={() => autofillAndLogin('customer@skynova.demo')}
              className="flex items-center justify-center space-x-1.5 text-xs font-black bg-orange-500 hover:bg-orange-600 text-white py-2.5 rounded-xl shadow-md transition-all hover:scale-105"
            >
              <User className="h-4 w-4" />
              <span>Customer Portal</span>
            </button>
            <button
              type="button"
              onClick={() => autofillAndLogin('admin@skynova.demo')}
              className="flex items-center justify-center space-x-1.5 text-xs font-black bg-slate-900 hover:bg-slate-800 text-orange-400 py-2.5 rounded-xl shadow-md transition-all hover:scale-105"
            >
              <ShieldCheck className="h-4 w-4 text-orange-400" />
              <span>Admin / Staff</span>
            </button>
          </div>
        </div>

        {error && (
          <div className="bg-rose-50 border border-rose-200 p-3 rounded-xl flex items-center space-x-2 text-xs font-semibold text-rose-700">
            <AlertCircle className="h-4 w-4 shrink-0" />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="text-xs font-bold text-slate-700 block mb-1">Email Address</label>
            <div className="relative">
              <Mail className="h-4 w-4 text-slate-400 absolute left-3 top-3.5" />
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                placeholder="customer@skynova.demo"
                className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-xl py-3 pl-10 pr-3 focus:ring-2 focus:ring-orange-500 outline-none"
              />
            </div>
          </div>

          <div>
            <label className="text-xs font-bold text-slate-700 block mb-1">Password</label>
            <div className="relative">
              <Lock className="h-4 w-4 text-slate-400 absolute left-3 top-3.5" />
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                placeholder="••••••••"
                className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-xl py-3 pl-10 pr-3 focus:ring-2 focus:ring-orange-500 outline-none"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full bg-orange-500 hover:bg-orange-600 text-white font-black text-xs py-3.5 rounded-xl shadow-lg shadow-orange-500/30 transition-all hover:scale-[1.01]"
          >
            {isSubmitting ? 'SIGNING IN...' : 'SIGN IN'}
          </button>
        </form>

        <p className="text-center text-xs text-slate-500">
          Don't have an account?{' '}
          <Link to="/register" className="font-bold text-orange-600 hover:text-orange-500">
            Create an Account
          </Link>
        </p>

      </div>
    </div>
  );
};
