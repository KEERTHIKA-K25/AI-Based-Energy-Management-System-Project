import React from 'react';
import './Footer.css';
import { FaPhone, FaEnvelope } from 'react-icons/fa'; // Import icons

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        {/* Left: Logo
       <div className="footer-contact">
          <a href="tel:+1234567890" className="footer-icon">
            <FaPhone />
          </a>
          <a href="mailto:info@company.com" className="footer-icon">
            <FaEnvelope />
          </a>
        </div> */}

        {/* Right: Copyright */}
        <div className="footer-text">
          <p>© Copyright 2025 - | All Rights Reserved | Powered by ecowatt</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
