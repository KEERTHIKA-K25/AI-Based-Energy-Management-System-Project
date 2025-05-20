// import './App.css';

// import React from "react";
// import HomePage from './Home';
// function App() {
//   return <HomePage/>;
// }


// export default App;




import './App.css';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import React from "react";
import Login from './Login/Login';
import Register from './Register/Register';
import Navbar from './Navbar';
import HomePage from './Home';
import Dashboard from './Dashboard/Dashboard';


function App() {
  return (
    <>
      <Router>
        <Navbar />
        <div> 
        <Routes>
          <Route path='/' element={<HomePage />} /> {/* ✅ Corrected */}
          <Route path='/Login' element={<Login />} />
          <Route path='/Register' element={<Register />} /> {/* ✅ Corrected */}
          <Route path='/Dashboard' element={<Dashboard />} />
        </Routes>
        </div>
      </Router>
    </>
  );
}

export default App;
