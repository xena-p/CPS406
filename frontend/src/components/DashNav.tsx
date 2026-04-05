import './styles/DashNav.css'

function DashNav(){
  return(
    <div className="nav-bar"> 
      <button className="nav-tab">Product Backlog</button>
      <button className="nav-tab">Sprints</button>
      <button className="nav-tab">Inbox</button>
    </div>
  );
}

export default DashNav