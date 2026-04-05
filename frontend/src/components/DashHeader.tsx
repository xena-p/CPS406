import logoutIcon from '../assets/logout-icon.webp'
import './styles/DashHeader.css'

function DashHeader(){
  return(
    <div className="header">
      <div className="title">
        <h1 className="project-name">Scrum Project Manager</h1>
        <h2 className="welcome-text">Welcome, dev@example.com (Developer)</h2>
      </div>
      <button className="logout-btn"> 
        <img src={logoutIcon} className="logout-icon" /> 
        Logout
      </button>
    </div>
  );
}

export default DashHeader