import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Register.css';

const Register = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    companyName: '',
    state: '',
    phoneNumber: ''
  });

  const [error, setError] = useState('');
  const navigate = useNavigate();

  // Handle input changes
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // Validate email format
  const isValidEmail = (email) => {
    return email.endsWith('@gmail.com');
  };

  // Validate phone number (exactly 10 digits)
  const isValidPhone = (phone) => {
    return /^\d{10}$/.test(phone);
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    // Check if all fields are filled
    if (!formData.email || !formData.password || !formData.companyName || !formData.state || !formData.phoneNumber) {
      setError('All fields are required.');
      return;
    }

    // Validate email
    if (!isValidEmail(formData.email)) {
      setError('Email must be a valid @gmail.com address.');
      return;
    }

    // Validate phone number
    if (!isValidPhone(formData.phoneNumber)) {
      setError('Phone number must be exactly 10 digits.');
      return;
    }

    // If all validations pass, send data to backend
    try {
      await axios.post('http://localhost:8080/api/auth/register', formData);
      alert('Registration successful!');
      navigate('/login');
    } catch (error) {
      alert('Registration failed. Email may already be in use.');
    }
  };

  return (
    <div className="register-page">
      <div className="register-box">
        <h2>Register</h2>
        <p className="register-subtitle">Create your account</p>

        {error && <p className="error-message">{error}</p>}

        <form onSubmit={handleSubmit}>
          <div className="input-container">
            <input
              type="email"
              name="email"
              placeholder="Business Email Id"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-container">
            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-container">
            <input
              type="text"
              name="companyName"
              placeholder="Company Name"
              value={formData.companyName}
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-container">
            <select
              name="state"
              value={formData.state}
              onChange={handleChange}
              required
            >
              <option value="" disabled>Select State</option>
              <option value="Andhra Pradesh">Andhra Pradesh</option>
              <option value="Bihar">Bihar</option>
              <option value="Karnataka">Karnataka</option>
              <option value="Maharashtra">Maharashtra</option>
              <option value="Tamil Nadu">Tamil Nadu</option>
            </select>
          </div>

          <div className="input-container">
            <input
              type="tel"
              name="phoneNumber"
              placeholder="Phone Number"
              value={formData.phoneNumber}
              onChange={handleChange}
              required
            />
          </div>

          <button type="submit" className="register-btn">Register</button>
        </form>

        <p className="login-link">
          Already have an account? <a href="/login">Login</a>
        </p>
      </div>
    </div>
  );
};

export default Register;
