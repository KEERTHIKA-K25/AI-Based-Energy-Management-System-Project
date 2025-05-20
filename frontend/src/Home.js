import React, { useEffect, useState } from "react";
import "./Homepage.css";
import { Link } from "react-router-dom";
import "./Card.css";
import { useScrollEffect } from "./Scroll"; // Import the scroll effect
import FeedbackMarquee from "./Feedback";
import Footer from "./Footer";

const VerticalCard = ({ title, description, image }) => (
  <div className="card-container">
    <div className="image-section">
      <img src={image} alt={title} className="card-image" />
    </div>
    <div className="content-section">
      <h2 className="card-title">{title}</h2>
      <p className="card-description">{description}</p>
    </div>
  </div>
);

const CardList = () => {
  const cardData = [
    {
      title: "Electricity Predictions",
      description: " Curious about next month’s electricity consumption? Our AI-powered prediction tool analyzes historical usage patterns and provides accurate forecasts. This helps you plan your energy usage in advance, avoid wastage, and manage costs more effectively. With reliable predictions, you stay prepared and avoid unexpected high bills.",
      image: "https://cdn.leonardo.ai/users/1c5e2d19-0d23-4f39-93ec-925021c1ab5d/generations/66b55b86-4e6a-430e-8c0e-d2afac509ca9/AlbedoBase_XL_A_modern_techinspired_background_illustrating_el_1.jpg?w=512",   
     },
    {
      title: "Bill Estimator",
      description: " No more guessing your electricity bill! Our bill estimation feature calculates your expected costs based on your energy usage trends and the latest electricity tariffs. This allows you to budget wisely, make cost-saving adjustments, and prevent bill shocks at the end of the month.",
      image: "https://cdn.leonardo.ai/users/1c5e2d19-0d23-4f39-93ec-925021c1ab5d/generations/66b55b86-4e6a-430e-8c0e-d2afac509ca9/AlbedoBase_XL_A_modern_techinspired_background_illustrating_el_0.jpg",
    },
    {
      title: "AI Recommendations",
      description: "By analyzing your consumption patterns, our AI suggests practical ways to reduce energy waste, improve efficiency, and lower costs. From adjusting appliance usage to identifying peak consumption hours, our recommendations help you take proactive steps toward smarter energy management.",
      image: "https://cdn.leonardo.ai/users/1c5e2d19-0d23-4f39-93ec-925021c1ab5d/generations/678d53a3-0d77-4cfa-b3a1-4597a574b9e4/AlbedoBase_XL_A_futuristic_digital_background_representing_AId_0.jpg",
    },
  ];

  return (
    <div className="card-list">
      {cardData.map((item, index) => (
        <VerticalCard
          key={index}
          title={item.title}
          description={item.description}
          image={item.image}
        />
      ))}
    </div>
  );
};

const CustomDashboards = () => {
  useScrollEffect(); // Apply scroll effect

  return (
    <div className="custom-dashboards hidden ">
      <div className="dashboard-image">
        <img src="/custom.gif" alt="Custom Dashboards" />
      </div>
      <div className="dashboard-text">
        <h4>Custom Dashboards</h4>
        <p>
          Get a personalized dashboard with real-time energy insights, helping
          you optimize electricity usage.
        </p>
      </div>
    </div>
  );
};


const HomePage = () => {
  const [scale, setScale] = useState(1);
  const [opacity, setOpacity] = useState(1); 

  


  return (
    <div >
      <style>
            <link rel="preconnect" href="https://fonts.googleapis.com"/>
            <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
            <link href="https://fonts.googleapis.com/css2?family=Tangerine:wght@400;700&display=swap&family=Nunito+Sans:ital,opsz,wght@0,6..12,200..1000;1,6..12,200..1000&family=Open+Sans:ital,wght@0,300..800;1,300..800&family=PT+Sans:ital,wght@0,400;0,700;1,400;1,700&family=PT+Serif:ital,wght@0,400;0,700;1,400;1,700&display=swap" rel="stylesheet"/>
        </style>

      <div className="home-container">

        {/* Overlay */}
        <div className="overlay"></div>

        {/* Main Section */}
        <h1 className="head-1">Smart Energy</h1>
        <h1 className="head-2">Management System</h1>
      </div>

      {/* Scrollable Content Below */}
      

<div className="scrollable-content">
        
        <div className="content-container">
          <div className="text-content">
            <h3 className="sub-title h3-home">What We Do?</h3>
            <p>
              At Ecowatt, we make energy management smarter and simpler. From personalized dashboards to AI-driven insights, we help you track your electricity usage, predict future consumption, estimate bills, and get smart recommendations—all in one place. Stay in control, save costs, and power a smarter future with us!
            </p>
          </div>
          <div className="video-container">
           </div>
          
          </div>
      

        <div className="gap-cont">
          <CustomDashboards/>
          </div>
          <div className="gap-cont">
            <CardList />
            </div>
            
            


        <div class="container gap-cont">
          <div class="div-pad">
              <h1 class="ways">Want to see your dashboard?</h1>
              <div class="flex">
                <Link to="/Login"><button class="button">Log in</button ></Link>
            
                 <Link to="/Register"> <button class="button">Register</button> </Link>
              </div>
          </div>
      </div>
      <Footer/>
      </div>
      
    </div>
  );
};

export default HomePage;
