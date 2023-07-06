import React from 'react';
import SearchBar from '../../component/common/SearchBar';
import Dropdown from '../../component/common/Dropdown';
import Pagination from '../../component/common/Pagination';
import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom/dist';
import { getMeLendingHistoryListApi } from '../../api/BookApi';

// TODO: 2번 렌더링 되는 문제가 있다
export default function MyLendingHistory() {
  const [searchParams, setSearchParams] = useSearchParams();
  const search = searchParams.get('search') || '';
  const [page, setPage] = useState(parseInt(searchParams.get('page')) - 1 || 0);
  const [size, setSize] = useState(parseInt(searchParams.get('size')) || 10);
  const [lendingHistoryList, setLendingHistoryList] = useState([]);
  const [total, setTotal] = useState(0);

  const handlePageChange = page => {
    setPage(page - 1);
    setSearchParams({ search, page: page, size });
  };

  useEffect(() => {
    getMeLendingHistoryListApi(search, page, size)
      .then(res => {
        setLendingHistoryList(res.data.myLendingHistory);
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

  const TableRow = ({ lending }) => (
    <tr>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {lending.bookTitle}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {lending.bookCallSign}
      </td>
      <td className="px-1 py-4 whitespace-nowrap text-sm text-black-500">
        <div className="flex items-center">
          <div className="text-sm font-medium text-gray-900">
            {lending.createdAt}
          </div>
        </div>
      </td>
      <td className="px-1 py-4 whitespace-nowrap text-sm text-black-500">
        <div className="flex items-center">
          <div className="ml-4">
            <div className="text-sm font-medium text-gray-900">
              {/* TODO: 반납 예정일과 반납 날짜는 다르게 해야 할 듯 */}
              {lending.returningEndAt ? lending.returningEndAt : '-'}
            </div>
          </div>
        </div>
      </td>
      <td className="px-8 py-4 whitespace-nowrap text-sm text-black-500">
        {lending.renew ? '1회' : '-'}
      </td>
    </tr>
  );

  return (
    <main className="flex-grow h-screen mx-32">
      <div className="flex justify-between items-center my-10 mx-48">
        <h1 className="text-2xl font-bold">대출/반납 검색</h1>
        <SearchBar
          text="제목을 입력해주세요."
          url="mypage/my-lending-history"
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
              제목
            </th>
            <th
              scope="col"
              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              청구기호
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
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {lendingHistoryList === [] ? (
            <tr>
              <td colSpan={5} className="text-center py-4">
                대출/반납 기록이 없습니다.
              </td>
            </tr>
          ) : (
            lendingHistoryList.map((lending, index) => (
              <TableRow key={index} lending={lending} />
            ))
          )}
        </tbody>
        <tfoot className="bg-white divide-y divide-gray-200">
          <tr>
            <td colSpan={6}>
              <div className="flex justify-center py-3">
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
            </td>
          </tr>
        </tfoot>
      </table>
    </main>
  );
}
