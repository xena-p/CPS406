import './styles/DashboardPage.css'
import DashHeader from './DashHeader.tsx'
import DashNav from './DashNav.tsx'
import ProductBacklog from './ProductBacklog.tsx'

function DashboardPage (){
  return(
    <div className="dashboard-page">
      <DashHeader />

      <div className="nav-section">
        <DashNav />
      </div>
      
      <div className="content-section">
        <ProductBacklog />
      </div>
    </div>
  );
}
export default DashboardPage