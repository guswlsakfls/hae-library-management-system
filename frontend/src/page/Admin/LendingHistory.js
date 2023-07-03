import Dropdown from '../../component/common/Dropdown';
import Pagination from '../../component/common/Pagination';
import SearchBar from '../../component/common/SearchBar';
import { useState, useEffect } from 'react';
import { getLendingListApi } from '../../api/BookApi';
import { useSearchParams } from 'react-router-dom/dist';

const TableRow = ({
  email,
  bookTitle,
  bookCallSign,
  lendingLibrarianEmail,
  lendingCondition,
  lendingDate,
  returningLibraryEmail,
  returningCondition,
  returningDate,
  extension,
  status,
}) => (
  <tr>
    <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
      {email}
    </td>
    <td className="py-4 whitespace-nowrap">
      <div className="px-1 flex items-center">
        <div className="ml-4">
          <div className="text-sm font-medium text-gray-900">{bookTitle}</div>
          <div className="text-sm text-gray-500">{bookCallSign}</div>
        </div>
      </div>
    </td>
    <td className="px-1 py-4 whitespace-nowrap text-sm text-black-500">
      <div className="flex items-center">
        <div className="ml-4">
          <div className="text-sm font-medium text-gray-900">{lendingDate}</div>
          <div className="text-sm font-medium text-gray-500">
            {lendingLibrarianEmail}
          </div>
          <div className="text-sm text-gray-500">{lendingCondition}</div>
        </div>
      </div>
    </td>
    <td className="px-1 py-4 whitespace-nowrap text-sm text-black-500">
      <div className="flex items-center">
        <div className="ml-4">
          <div className="text-sm font-medium text-gray-900">
            {returningDate}
          </div>
          <div className="text-sm font-medium text-gray-500">
            {returningLibraryEmail}
          </div>
          <div className="text-sm text-gray-500">{returningCondition}</div>
        </div>
      </div>
    </td>
    <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
      {extension}
    </td>
    <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
      {status}
    </td>
  </tr>
);

export default function LendingHistory() {
  const [lendingList, setLendingList] = useState([]);
  const [searchParams, setSearchParams] = useSearchParams();
  const search = searchParams.get('search') || '';
  const [page, setPage] = useState(parseInt(searchParams.get('page')) - 1 || 0);
  const [size, setSize] = useState(parseInt(searchParams.get('size')) || 10);
  const [total, setTotal] = useState(0);

  const handlePageChange = page => {
    setPage(page - 1);
    setSearchParams({ search, page: page, size });
  };

  useEffect(() => {
    getLendingListApi(search, page, size)
      .then(res => {
        setLendingList(res.data.lendingList);
        setTotal(res.data.totalElements);
        setPage(res.data.currentPage);
        setSize(res.data.size);
        console.log(res);
      })
      .catch(err => {
        if (err.response.status === 401 || err.response.status === 403) {
          alert('로그인이 필요합니다.');
          window.location.href = '/login';
          return;
        }
        console.log(err.response);
        alert(err.response.data.message);
      });
  }, [search, page, size]);

  return (
    <main className="flex-grow h-screen overflow-y-scroll">
      <div className="flex justify-center items-center my-10 mx-48">
        <h1 className="text-4xl font-bold">대출/반납 기록</h1>
      </div>
      <div className="flex justify-between items-center my-10 mx-48">
        <h1 className="text-2xl font-bold">대출/반납 검색</h1>
        <SearchBar
          text="제목 또는 청구기호로 검색해 주세요."
          url="admin/lending-history"
        ></SearchBar>
        <div className="flex">
          <div className="mr-2">
            <Dropdown option1="대출일 순"></Dropdown>
          </div>
          <div className="mr-2">
            <Dropdown option1="대출중"></Dropdown>
          </div>
        </div>
      </div>
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
          <tr>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              대출 신청자
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              도서정보
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              대출일
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              반납일
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              연장
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              상태
            </th>
            <th scope="col" className="relative px-6 py-3">
              <span className="sr-only">Edit</span>
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {lendingList.map((item, index) => (
            <TableRow
              key={index}
              email={item.userEmail}
              bookTitle={item.bookTitle}
              bookCallSign={item.bookCallSign}
              lendingLibrarianEmail={item.lendingLibrarianEmail}
              lendingCondition={item.lendingCondition}
              lendingDate={item.createdAt}
              returningLibraryEmail={item.returningLibrarianEmail}
              returningCondition={item.returningCondition}
              returningDate={item.returningAt}
              extension={item.renew ? '1회' : '-'}
              status={
                item.returningLibrarianEmail != null ? '반납완료' : '대출중'
              }
            />
          ))}
        </tbody>
        <tfoot className="bg-white divide-y divide-gray-200">
          <tr>
            <td colSpan={6}>
              <div className="flex justify-center py-3">
                <div className="mt-3">
                  <Pagination
                    activePage={page + 1}
                    itemsCountPerPage={size}
                    totalItemsCount={(total / size) * 10}
                    pageRangeDisplayed={5}
                    prevPageText={'‹'}
                    nextPageText={'›'}
                    handlePageChange={handlePageChange}
                  ></Pagination>
                </div>
              </div>
            </td>
          </tr>
        </tfoot>
      </table>
    </main>
  );
}
