import { useState } from 'react'
import './styles/DashboardPage.css'
import DashHeader from './DashHeader.tsx'
import DashNav from './DashNav.tsx'
import ProductBacklog from './ProductBacklog.tsx'
import SprintView from './SprintView.tsx'
import InboxView from './InboxView.tsx'
import ReportsView from './ReportsView.tsx'

function DashboardPage(){
  const [activeTab, setActiveTab] = useState('backlog');

  return(
    <div className="dashboard-page">
      <DashHeader />

      <div className="nav-section">
        <DashNav activeTab={activeTab} onTabChange={setActiveTab} />
      </div>

      <div className="content-section">
        {activeTab === 'backlog'  && <ProductBacklog />}
        {activeTab === 'sprints'  && <SprintView />}
        {activeTab === 'inbox'    && <InboxView />}
        {activeTab === 'reports'  && <ReportsView />}
      </div>
    </div>
  );
}

export default DashboardPage
