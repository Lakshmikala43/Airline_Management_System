import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Plane, User, Mail, Lock, Phone, CreditCard, AlertCircle } from 'lucide-react';

export const RegisterPage: React.FC = () => {
  const { register, login } = useAuth();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    phoneNumber: '',
    passportNumber: '',
  });

  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsSubmitting(true);

    try {
      await register(formData);
      await login({ email: formData.email, password: formData.password });
      navigate('/dashboard');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Registration failed.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-4 sm:px-6 lg:px-8 py-12">
      <div className="max-w-lg w-full bg-white rounded-3xl shadow-2xl border border-slate-200 p-8 space-y-6">
        
        <div className="text-center space-y-2">
          <div className="w-12 h-12 bg-orange-500 rounded-2xl text-white flex items-center justify-center mx-auto shadow-lg shadow-orange-500/30">
            <Plane className="h-6 w-6 transform -rotate-45" />
          </div>
          <h2 className="text-2xl font-black text-slate-900">Create Passenger Profile</h2>
          <p className="text-xs text-slate-500 font-medium">Join SkyNova Airways for fast web check-in and e-ticket downloads</p>
        </div>

        {error && (
          <div className="bg-rose-50 border border-rose-200 p-3 rounded-xl flex items-center space-x-2 text-xs font-semibold text-rose-700">
            <AlertCircle className="h-4 w-4 shrink-0" />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="text-xs font-bold text-slate-700 block mb-1">First Name *</label>
              <input
                type="text"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                required
                placeholder="Lakshmi"
                className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-xl p-3 outline-none focus:ring-2 focus:ring-orange-500"
              />
            </div>
            <div>
              <label className="text-xs font-bold text-slate-700 block mb-1">Last Name *</label>
              <input
                type="text"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                required
                placeholder="Kala"
                className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-xl p-3 outline-none focus:ring-2 focus:ring-orange-500"
              />
            </div>
          </div>

          <div>
            <label className="text-xs font-bold text-slate-700 block mb-1">Email Address *</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              placeholder="name@example.com"
              className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-xl p-3 outline-none focus:ring-2 focus:ring-orange-500"
            />
          </div>

          <div>
            <label className="text-xs font-bold text-slate-700 block mb-1">Password *</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              placeholder="••••••••"
              className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-xl p-3 outline-none focus:ring-2 focus:ring-orange-500"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="text-xs font-bold text-slate-700 block mb-1">Phone Number</label>
              <input
                type="tel"
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                placeholder="+91 9876543210"
                className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-xl p-3 outline-none focus:ring-2 focus:ring-orange-500"
              />
            </div>
            <div>
              <label className="text-xs font-bold text-slate-700 block mb-1">Passport / ID</label>
              <input
                type="text"
                name="passportNumber"
                value={formData.passportNumber}
                onChange={handleChange}
                placeholder="Z9876543"
                className="w-full text-xs font-semibold bg-slate-50 border border-slate-200 rounded-xl p-3 outline-none focus:ring-2 focus:ring-orange-500"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full bg-orange-500 hover:bg-orange-600 text-white font-black text-xs py-3.5 rounded-xl shadow-lg shadow-orange-500/30 transition-all hover:scale-[1.01]"
          >
            {isSubmitting ? 'CREATING ACCOUNT...' : 'REGISTER PASSENGER PROFILE'}
          </button>
        </form>

        <p className="text-center text-xs text-slate-500">
          Already have an account?{' '}
          <Link to="/login" className="font-bold text-orange-600 hover:text-orange-500">
            Sign In
          </Link>
        </p>

      </div>
    </div>
  );
};
