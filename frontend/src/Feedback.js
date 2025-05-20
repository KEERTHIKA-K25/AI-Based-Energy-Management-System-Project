import { motion } from "framer-motion";
import "./Feedback.css";
const feedbacks = [
  "This AI-driven energy management system saved me 30% on my electricity bill!",
  "The smart recommendations are super helpful. I love how easy it is to use!",
  "Real-time insights helped me reduce wastage. Highly recommended!",
  "This system is a game-changer for optimizing electricity consumption!",
];

 



function FeedbackMarquee() {
    return (
      <div className="w-full bg-gray-900 py-4 overflow-hidden whitespace-nowrap">
        <motion.div
          className="flex space-x-16 text-lg font-semibold text-green-400"
          initial={{ x: "100%" }}
          animate={{ x: "-100%" }}
          transition={{
            repeat: Infinity,
            duration: 15,
            ease: "linear",
          }}
        >
          {feedbacks.map((feedback, index) => (
            <span key={index} className="feed-content">
              "{feedback}" 
            </span>
          ))}
        </motion.div>
      </div>
    );
  }
export default FeedbackMarquee;