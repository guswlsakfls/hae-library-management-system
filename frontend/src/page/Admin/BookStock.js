import React, { useState, useEffect } from 'react';
import Dropdown from '../../component/common/Dropdown';
import Pagination from '../../component/common/Pagination';
import SearchBar from '../../component/common/SearchBar';
import DefaultButton from '../../component/common/DefaultButton';
import { useSearchParams } from 'react-router-dom/dist';
import { getBookStockListApi, updateBookStockApi } from '../../api/BookApi';

const statusText = {
  FINE: '양호',
  BREAK: '손상',
  LOST: '분실',
};

export default function BookStock() {
  const [isOpen, setIsOpen] = useState(false);
  const [searchParams, setSearchParams] = useSearchParams();
  const search = searchParams.get('search') || '';
  const [page, setPage] = useState(parseInt(searchParams.get('page')) - 1 || 0);
  const [size, setSize] = useState(parseInt(searchParams.get('size')) || 10);
  const [bookStockList, setBookStockList] = useState([]);
  const [total, setTotal] = useState(0);
  // 수정할 책 정보를 관리하는 state 추가
  const [editBook, setEditBook] = useState(null);

  function toggleModal() {
    setIsOpen(!isOpen);
  }
  // 수정 버튼 클릭 시 호출되는 함수
  const handleEditClick = book => {
    setEditBook(book);
    toggleModal();
    console.log(book);
  };

  const handlePageChange = page => {
    setPage(page - 1);
    setSearchParams({ search, page: page, size });
  };

  const handleInputChange = event => {
    console.log(event.target.name, event.target.value);
    const { name, value } = event.target;
    setEditBook(prevBook => {
      // 'bookInfo' 객체에 속한 속성인지 확인
      if (
        [
          'title',
          'author',
          'publisher',
          'category',
          'isbn',
          'image',
          'publishedAt',
        ].includes(name)
      ) {
        return {
          ...prevBook,
          bookInfo: {
            ...prevBook.bookInfo,
            [name]: value,
          },
        };
      }
      // 그렇지 않다면, 직접 변경
      else {
        return {
          ...prevBook,
          [name]: value,
        };
      }
    });
    console.log(editBook);
  };

  const onSubmit = e => {
    updateBookStockApi(editBook)
      .then(res => {
        console.log(res);
        alert(res.message);
        toggleModal();
        window.location.reload();
      })
      .catch(err => {
        console.log(err.response);
        if (err.response.status === 401 || err.response.status === 403) {
          alert('로그인이 필요합니다.');
          window.location.href = '/login';
          return;
        }
        alert(err.response.data.message);
        let errors = err.response.data.errors;
        if (!errors) {
          return;
        }
        let errorMessages = errors
          .map((error, index) => `${index + 1}. ${error.message}`)
          .join('\n\n');
        alert(errorMessages);
      });
  };

  useEffect(() => {
    getBookStockListApi(search, page, size)
      .then(res => {
        setBookStockList(res.data.bookList);
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

  const TableRow = ({ book }) => (
    <tr>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.bookInfo.title}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.callSign}
      </td>

      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.lending ? '대출중' : '대출가능'}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {statusText[book.status]}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.createdAt}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.updatedAt}
      </td>
      <td className="py-4 whitespace-nowrap text-sm text-black-500">
        <DefaultButton size="small" onClick={() => handleEditClick(book)}>
          수정
        </DefaultButton>
      </td>
    </tr>
  );

  return (
    <>
      <main className="flex-grow h-screen overflow-y-scroll">
        <div className="flex justify-center items-center my-10 mx-48">
          <h1 className="text-4xl font-bold">도서 관리</h1>
        </div>
        <div className="flex justify-between items-center my-10 mx-48">
          <h1 className="text-2xl font-bold">보유 도서 검색</h1>
          <SearchBar text="청구기호를 입력해 주세요."></SearchBar>
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
                대출여부
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                상태
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                등록일
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                수정일
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              ></th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {bookStockList &&
              bookStockList.map((book, index) => (
                <TableRow key={index} book={book} />
              ))}
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
      {isOpen && (
        <div className="fixed top-0 left-0 right-0 bottom-0 flex items-center justify-center z-50 bg-gray-500 bg-opacity-50">
          <div
            className="bg-white rounded-lg shadow-xl p-6 border border-gray-200 dark:border-gray-500"
            style={{
              width: 'fit-content',
              maxWidth: '80vw',
              height: 'fit-content',
              maxHeight: '100vh',
              overflow: 'auto',
            }}
          >
            {/* 모달 내용 */}
            <div className="flex justify-center items-center my-1 mx-48">
              <h1 className="text-2xl font-bold">도서 수정</h1>
            </div>
            <div className="flex flex-col">
              <div className="flex flex-row justify-between">
                <div className="flex flex-col">
                  <label
                    htmlFor="title"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    제목
                  </label>
                  <input
                    type="text"
                    name="title"
                    id="title"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mr-96 text-gray-500"
                    placeholder="제목을 입력하세요."
                    value={editBook.bookInfo.title}
                    onChange={handleInputChange}
                  />
                  <label
                    htmlFor="author"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    저자
                  </label>
                  <input
                    type="text"
                    name="author"
                    id="author"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mr-96 text-gray-500"
                    placeholder="저자를 입력하세요."
                    value={editBook.bookInfo.author}
                    onChange={handleInputChange}
                  />
                  <label
                    htmlFor="publisher"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    출판사
                  </label>
                  <input
                    type="text"
                    name="publisher"
                    id="publisher"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mr-96 text-gray-500"
                    placeholder="출판사를 입력하세요."
                    value={editBook.bookInfo.publisher}
                    onChange={handleInputChange}
                  />
                  <label
                    htmlFor="category"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    카테고리
                  </label>
                  <input
                    type="text"
                    name="category"
                    id="category"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mr-96 text-gray-500"
                    placeholder="카테고리를 입력하세요."
                    value={editBook.bookInfo.category}
                    onChange={handleInputChange}
                  />
                  <label
                    htmlFor="isbn"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    ISBN
                  </label>
                  <input
                    type="text"
                    name="isbn"
                    id="isbn"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mr-96 text-gray-500"
                    placeholder="isbn을 입력하세요."
                    value={editBook.bookInfo.isbn}
                    onChange={handleInputChange}
                  />
                  <label
                    htmlFor="callSign"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    청구기호
                  </label>
                  <input
                    type="text"
                    name="callSign"
                    id="callSign"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mr-96 text-gray-500"
                    placeholder="청구기호를 입력하세요."
                    value={editBook.callSign}
                    onChange={handleInputChange}
                  />

                  <label
                    htmlFor="status"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    상태
                  </label>
                  <select
                    id="role"
                    name="role"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md text-gray-500"
                    value={editBook.status}
                    onChange={handleInputChange}
                  >
                    <option value="BOOK_FINE">보통</option>
                    <option value="BOOK_BREAK">파손</option>
                  </select>

                  <label
                    htmlFor="donator"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    기부자
                  </label>
                  <input
                    type="text"
                    name="donator"
                    id="donator"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md text-gray-500"
                    placeholder="기부자를 입력하세요."
                    value={editBook.donator}
                    onChange={handleInputChange}
                  />

                  <label
                    htmlFor="image"
                    className="block text-sm font-medium text-gray-700 mt-3"
                  >
                    이미지 url
                  </label>
                  <input
                    type="text"
                    name="image"
                    id="image"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mb-5 text-gray-500"
                    placeholder="이미지 주소를 입력하세요."
                    value={editBook.bookInfo.image}
                    onChange={handleInputChange}
                  />
                </div>
              </div>
              {/* 버튼 */}
              <div className="mt-4 flex justify-end">
                <DefaultButton onClick={toggleModal} color={'red'}>
                  취소
                </DefaultButton>

                <DefaultButton onClick={onSubmit} color={'blue'}>
                  수정
                </DefaultButton>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
