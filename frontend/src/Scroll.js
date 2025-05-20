import { useEffect } from "react";

const handleScrollAnimation = () => {
  const sections = document.querySelectorAll(".custom-dashboards");

  sections.forEach((section) => {
    const rect = section.getBoundingClientRect();
    const windowHeight = window.innerHeight;

    console.log("Checking visibility:", rect.top, windowHeight); // Debugging log

    if (rect.top < windowHeight * 0.75 && rect.bottom > windowHeight * 0.25) {
      console.log("Adding visible class"); // Debug log
      section.classList.add("visible");
      section.classList.remove("hidden");
    } else {
      console.log("Adding hidden class"); // Debug log
      section.classList.remove("visible");
      section.classList.add("hidden");
    }
  });
};

export const useScrollEffect = () => {
  useEffect(() => {
    window.addEventListener("scroll", handleScrollAnimation);
    handleScrollAnimation(); // Run on mount to check visibility
    return () => window.removeEventListener("scroll", handleScrollAnimation);
  }, []);
};