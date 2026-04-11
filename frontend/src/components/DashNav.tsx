import './styles/DashNav.css'

type DashNavProps = {
  activeTab: string;
  onTabChange: (tab: string) => void;
}

function DashNav({ activeTab, onTabChange }: DashNavProps){
  return(
    <div className="nav-bar">
      <button
        className={`nav-tab ${activeTab === 'backlog' ? 'nav-tab-active' : ''}`}
        onClick={() => onTabChange('backlog')}
      >Product Backlog</button>
      <button
        className={`nav-tab ${activeTab === 'sprints' ? 'nav-tab-active' : ''}`}
        onClick={() => onTabChange('sprints')}
      >Sprints</button>
      <button
        className={`nav-tab ${activeTab === 'inbox' ? 'nav-tab-active' : ''}`}
        onClick={() => onTabChange('inbox')}
      >Inbox</button>
      <button
        className={`nav-tab ${activeTab === 'reports' ? 'nav-tab-active' : ''}`}
        onClick={() => onTabChange('reports')}
      >Reports</button>
    </div>
  );
}

export default DashNav
