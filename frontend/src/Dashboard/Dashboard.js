import React from 'react';
import { useState, useEffect } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import './Dashboard.css';
import { FaLightbulb, FaSnowflake, FaLaptop, FaBuilding, FaFan, FaEllipsisH } from "react-icons/fa";
import { Lightbulb, Fan, Snowflake, MonitorSmartphone } from "lucide-react";
import { motion } from "framer-motion";

const Dashboard = () => {
  const [dashboardLink, setDashboardLink] = useState('');
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedHour, setSelectedHour] = useState("");
  const [loading, setLoading] = useState(false); // New loading state
  const [recommendations, setRecommendations] = useState([]);


   // Bill Estimation State
   const [billData, setBillData] = useState([]);
   const [totalBill, setTotalBill] = useState("₹0.00");
   const [totalUnits, setTotalUnits] = useState("0 kWh");
  
  const [usageData, setUsageData] = useState([
    { icon: <FaLightbulb />, category: "Light", value: "0 kWh" },
    { icon: <FaSnowflake />, category: "AC", value: "0 kWh" },
    { icon: <FaLaptop />, category: "Computers", value: "0 kWh" },
    { icon: <FaBuilding />, category: "Lift", value: "0 kWh" },
    { icon: <FaFan />, category: "Fan", value: "0 kWh" },
    { icon: <FaEllipsisH />, category: "Others", value: "0 kWh" },
  ]);

  useEffect(() => {
    const link = localStorage.getItem("dashboardLink"); 
    if (link) {
      setDashboardLink(link);
    }

    fetchBillEstimation();
    fetchRecommendations();
  }, []);

  // Updated fetchPredictions function with loading state
  const fetchPredictions = async () => {
    if (!selectedDate || selectedHour === "") {
      alert("Please select both date and hour.");
      return;
    }

    setLoading(true); // Show loading while fetching
    const formattedDate = selectedDate.toISOString().split('T')[0];
    const email = localStorage.getItem('email');
    
    if (!email) {
      console.error("Email not found in localStorage!");
      alert("You are not logged in. Please log in again.");
      setLoading(false);
      return;
    }

    const apiUrl = `http://localhost:8080/api/predictions?date=${formattedDate}&hour=${selectedHour}&email=${email}`;
    console.log("Fetching from:", apiUrl);
    console.log("API Request:", { formattedDate, selectedHour, email, apiUrl }); // Log request data
    
    try {
      const response = await fetch(apiUrl);
      if (!response.ok) {
        throw new Error(`Failed to fetch predictions: ${response.status}`);
      }
      const data = await response.json();
      console.log("API Response:", data); // Debug log

      
    // Map response keys to match categories exactly
    const categoryMap = {
      Light: "Light (kWh)",
      AC: "AC (kWh)",
      Computers: "Computers (kWh)",
      Lift: "Lift (kWh)",
      Fan: "Fan (kWh)",
      Others: "Other (kWh)" // Note: "Other", not "Others"!
    };
    

      // Update only the values within the existing cards
      setUsageData((prevData) =>
        prevData.map((item) => {
          const apiKey = categoryMap[item.category];
          const value = data[apiKey] ?? "N/A"; // Handle missing keys safely
          //const value = data[categoryMap[item.category]] || "N/A";
          console.log(`Updating ${item.category}:`, value); // Log updates
          return {
            ...item,
            value: `${value !== "N/A" ? value.toFixed(2) : "N/A"} kWh`, // Format numbers to 2 decimals
          };
        })
      );
    } catch (error) {
      console.error("Error fetching predictions:", error);
      alert("Failed to fetch predictions.");
    } finally {
      setLoading(false); // Stop loading after fetch
    }
  };

  const getIcon = (category) => {
    switch (category) {
      case "Light (kWh)": return <FaLightbulb />;
      case "AC (kWh)": return <FaSnowflake />;
      case "Computers (kWh)": return <FaLaptop />;
      case "Lift (kWh)": return <FaBuilding />;
      case "Fan (kWh)": return <FaFan />;
      case "Other (kWh)": return <FaEllipsisH />;
      default: return <FaEllipsisH />;
    }
  };
  
  // Function to Fetch Bill Estimation Data
const fetchBillEstimation = async () => {
  const email = localStorage.getItem("email");

  if (!email) {
    console.error("Email not found in localStorage!");
    alert("You are not logged in. Please log in again.");
    return;
  }

  const apiUrl = `http://localhost:8080/api/bill-estimation?email=${email}`;
  console.log("Fetching from:", apiUrl);

  try {
    const response = await fetch(apiUrl);
    if (!response.ok) {
      throw new Error(`Failed to fetch bill estimation: ${response.status}`);
    }
    const data = await response.json();
    console.log("Bill Estimation Response:", data);

    // Extract and Update Bill Data
    const categories = [
      "Light (kWh)", "AC (kWh)", "Computers (kWh)", "Lift (kWh)", "Fan (kWh)", "Other (kWh)"
    ];
    
    const updatedBillData = categories.map(category => ({
      icon: getIcon(category),
      category,
      value: data[category] ? data[category].split("|")[0].trim() : "0 kWh",  // Extract kWh value
      amt: data[category] ? data[category].split("|")[1].trim().replace("₹", "Rs.") : "Rs. 0.00"
 // Extract ₹ amount
    }));

    setBillData(updatedBillData);
    setTotalUnits(data["Total Units Consumed"] || "0 kWh");
    setTotalBill(data["Total Bill"] ? data["Total Bill"].replace("₹", "Rs.") : "Rs. 0.00");


  } catch (error) {
    console.error("Error fetching bill estimation:", error);
    alert("Failed to fetch bill estimation.");
  }
};

  const usageBill = billData;  // Use fetched bill estimation data


  //   Bill estimator
  const usageBillTotal = [{ val: totalUnits, amt: totalBill }];

  // Function to Fetch AI Recommendations

  const categoryIcons = {
    Light: <FaLightbulb />,
    Fan: <FaFan />,
    AC: <FaSnowflake />,
    Computers: <FaLaptop />,
    Lift: <FaBuilding />,
    Other: <FaEllipsisH />
  };
  
const fetchRecommendations = async () => {
  const email = localStorage.getItem("email");

  if (!email) {
    console.error("Email not found in localStorage!");
    alert("You are not logged in. Please log in again.");
    return;
  }

  const apiUrl = `http://localhost:8080/api/recommendations?email=${email}`;
  console.log("Fetching from:", apiUrl);

  try {
    const response = await fetch(apiUrl);
    if (!response.ok) {
      throw new Error(`Failed to fetch recommendations: ${response.status}`);
    }
    const data = await response.json();
    console.log("AI Recommendations Response:", data);
     // Convert response into array and add icons
     const recommendationsArray = Object.entries(data).map(([category, text]) => ({
      title: category,
      text: text,
      icon: categoryIcons[category] || <FaEllipsisH /> // Default icon if category not found
    }));

   
    setRecommendations(recommendationsArray);
  } catch (error) {
    console.error("Error fetching recommendations:", error);
    alert("Failed to fetch AI recommendations.");
  }
};





  return (
    <div>
        <style>
            <link rel="preconnect" href="https://fonts.googleapis.com" />
            <link rel="preconnect" href="https://fonts.gstatic.com" crossOrigin="true" />
            <link href="https://fonts.googleapis.com/css2?family=Tangerine:wght@400;700&display=swap&family=Nunito+Sans:ital,opsz,wght@0,6..12,200..1000;1,6..12,200..1000&family=Open+Sans:ital,wght@0,300..800;1,300..800&family=PT+Sans:ital,wght@0,400;0,700;1,400;1,700&family=PT+Serif:ital,wght@0,400;0,700;1,400;1,700&display=swap" rel="stylesheet" />
        </style>

        {/* Dashboard Section */}
        <section className="container-dash">
          <h1 className="title">Dashboard</h1>
          {dashboardLink ? (
            <iframe
              width="950"
              height="600"
              src={dashboardLink}
              frameBorder="0"
              allowFullScreen
              sandbox="allow-scripts allow-same-origin allow-popups allow-popups-to-escape-sandbox"
              title="Energy Dashboard"
            ></iframe>
          ) : (
            <p>No dashboard link available</p>
          )}
        </section>

        {/* Electricity Prediction Section */}
        <section className='container-dash'>
          <h1 className="title">Electricity Prediction</h1>
          <div className="input-container-dash">
            <div className="input-group-dash">
              <label>Select a date:</label>
              <DatePicker
                selected={selectedDate}
                onChange={(date) => setSelectedDate(date)}
                dateFormat="yyyy-MM-dd"
                className="input-box-dash"
                placeholderText="Date"
              />
            </div>
            <div className="input-group-dash">
              <label>Select an hour:</label>
              <select
                className="input-box-dash"
                value={selectedHour}
                onChange={(e) => setSelectedHour(e.target.value)}
              >
                <option value="">Hour</option>
                {[...Array(24).keys()].map((hour) => (
                  <option key={hour} value={hour}>
                    {hour}
                  </option>
                ))}
              </select>
            </div>
            <div className="input-group-dash">
              <button onClick={fetchPredictions} className="input-box-dash">
                {loading ? "Fetching..." : "Get Predictions"}
              </button>
            </div>
          </div>

          <div className="grid-container">
            {usageData.map((item, index) => (
              <div key={index} className="usage-card">
                <div className="icon">{item.icon}</div>
                <h3 className="category">{item.category} (kWh)</h3>
                <h6 className="value-dash">{item.value}</h6>
              </div>
            ))}
          </div>
        </section>

        {/* Bill Estimator Section */}
        <section className='container-dash'>
          <h1 className="title">Electricity Bill Estimator</h1>
          <div className="grid-container">
            {usageBill.map((item, index) => (
              <div key={index} className="usage-card">
                <div className="icon">{item.icon}</div>
                <h3 className="category">{item.category}</h3>
                <h6 className="value-dash">{item.value}</h6>
                <h6 className="value-dash">{item.amt}</h6> 
              </div>
            ))}
          </div>
          <div>
            {usageBillTotal.map((item, index) => (
                <div className='flex f-gap'>
                  <h3 className="total-info">Total kwh: {item.val}</h3>
                  <h3 className="total-info">Total Amount: {item.amt}</h3>
                </div>
                ))}
          </div>

        </section>

        {/* AI Recommendations Section */}
{/* AI Recommendations Section */}
<section className='recommendations-container'>
    <h1 className="recommendations-title">AI Recommendations</h1>
    <div className="recommendations-grid">
        {recommendations.length > 0 ? (
            recommendations.map((rec, index) => (
                <motion.div
                    key={index}
                    whileHover={{ scale: 1.1 }}
                    whileTap={{ scale: 0.95 }}
                    className="recommendation-card"
                >
                    <div className="recommendation-icon">
                        {rec.icon} 
                    </div>
                    <h3 className="recommendation-title">{rec.title}</h3>
                    <p className="recommendation-description">{rec.text}</p>
                </motion.div>
            ))
        ) : (
            <p className="text-gray-400">No recommendations available</p>
        )}
    </div>
</section>


    </div>
  );
};

export default Dashboard;
