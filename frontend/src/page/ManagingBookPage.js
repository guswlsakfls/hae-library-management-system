import SideBar from '../component/SideBar';
import BookStock from './Admin/ManagingBook';

const ManagingBookPage = () => (
  <div className="flex">
    <SideBar />
    <BookStock />
  </div>
);

export default ManagingBookPage;
