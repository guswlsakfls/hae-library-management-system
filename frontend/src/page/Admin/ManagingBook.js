import React, { useState, useEffect } from 'react';
import Dropdown from '../../component/common/Dropdown';
import Pagination from '../../component/common/Pagination';
import SearchBar from '../../component/common/SearchBar';
import DefaultButton from '../../component/common/DefaultButton';
import { useSearchParams } from 'react-router-dom/dist';
import {
  getBookListApi,
  getBookStockListApi,
  updateBookStockApi,
  deleteBookApi,
} from '../../api/BookApi';
import { getCategoryListApi } from '../../api/CategoryApi';
import SelectBar from '../../component/common/SelectBar';

const statusText = {
  FINE: '양호',
  BREAK: '손상',
  LOST: '분실',
};

// 이름순, 날짜순 정렬
const sortList = ['최신도서', '오래된도서'];

export default function ManagingBook() {
  const [isOpen, setIsOpen] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [searchParams, setSearchParams] = useSearchParams();
  const search = searchParams.get('search') || '';
  const [page, setPage] = useState(parseInt(searchParams.get('page')) - 1 || 0);
  const [size, setSize] = useState(parseInt(searchParams.get('size')) || 10);
  const [bookStockList, setBookStockList] = useState([]); // 도서 목록을 저장하는 state
  const [bookInfoList, setBookInfoList] = useState([]);
  const [total, setTotal] = useState(0);
  const [category, setCategory] = useState('전체');
  const [sort, setSort] = useState('최신순');
  // 수정할 책 정보를 관리하는 state 추가
  const [editBook, setEditBook] = useState(null);
  const [categoryList, setCategoryList] = useState([]);

  const toggleModal = () => {
    setIsOpen(!isOpen);
    setIsEdit(false);
  };

  const toggleEditModal = () => {
    setIsEdit(true);
  };

  const toggleEditCloseModal = () => {
    setIsEdit(false);
  };

  // 도서 목록을 조회하는 함수
  const handleEditClick = (bookStockList, id) => {
    console.log('bookStockList: ', bookStockList);
    toggleEditModal();

    // bookStockList에서 bookList를 제거하고 bookInfo로 이름을 바꾸는 부분
    let editBook = { ...bookStockList };
    delete editBook.bookList;

    // id에 관련된 bookList에서 항목을 찾는 부분
    const targetBook = bookStockList.bookList.find(book => book.id === id);
    if (targetBook) {
      editBook = { ...editBook, ...targetBook };
    }

    setEditBook(editBook);
    console.log('editBook: ', editBook);
  };

  // 수정 버튼 클릭 시 호출되는 함수
  const handleReadClick = id => {
    console.log(id);
    // // 카테고리 조회 합니다.
    // getCategoryListApi()
    //   .then(res => {
    //     console.log(res);
    //     setCategoryList(res.data);
    //   })
    //   .catch(err => {
    //     console.log(err.response);
    //     alert(err.response.data.message);
    //   });

    // // 책 정보 조회 합니다.
    getBookStockListApi(id)
      .then(res => {
        console.log(res);
        setBookStockList(res.data);
      })
      .catch(err => {
        console.log(err.response);
        alert(err.response.data.message);
      });

    toggleModal();
  };

  const handlePageChange = page => {
    setPage(page - 1);
    setSearchParams({ search, page: page, size });
  };

  const handleInputChange = event => {
    console.log(event.target.name, event.target.value);
    const { name, value } = event.target;
    setEditBook({
      ...editBook,
      [name]: value,
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

  // 도서 삭제 신청
  const handleDeleteClick = id => {
    console.log(id);
    if (
      window.confirm(
        '[!주의!] 대출 관련 기록도 삭제됩니다. 정말 삭제하시겠습니까?'
      )
    ) {
      deleteBookApi(id)
        .then(res => {
          console.log(res);
          alert(res.message);
          window.location.reload();
        })
        .catch(err => {
          console.log(err.response);
          alert(err.response.data.message);
        });
    }
  };

  useEffect(() => {
    getBookListApi(search, page, size, category, sort)
      .then(res => {
        setBookInfoList(res.data.bookInfoList);
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

    // 카테고리 목록을 불러오는 함수
    getCategoryListApi()
      .then(res => {
        console.log('selecti: ', res);
        setCategoryList([{ value: 0, categoryName: '전체' }, ...res.data]);
      })
      .catch(err => {
        console.log(err);
        alert('카테고리 목록을 불러오는데 실패했습니다.');
      });
  }, [search, page, size, category, sort]);

  const TableRow = ({ book }) => (
    <tr>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.title}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.isbn}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.categoryName}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
        {book.stockQuantity + ' 권'}
      </td>
      <td className="py-4 whitespace-nowrap text-sm text-black-500">
        <DefaultButton
          color={'blue'}
          size="small"
          onClick={() => handleReadClick(book.id)}
        >
          조회
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
          <h1 className="text-2xl font-bold">도서 검색</h1>
          <SearchBar
            text="제목 또는 ISBN로 검색해 주세요."
            url="admin/book-stock"
          ></SearchBar>
          <div className="flex mx-4 mt-2">
            <SelectBar
              width="120"
              value={category}
              onChange={e => {
                setCategory(e.target.value);
                setPage(0); // 카테고리 변경 시, 페이지를 1로 설정 (0-based index이기 때문에 0)
                setSearchParams({
                  search,
                  page: 1, // 카테고리 변경 시, 페이지를 1로 설정
                  size,
                  category: e.target.value,
                  sort,
                });
              }}
              items={categoryList}
            ></SelectBar>
            <div className="mx-2">
              <SelectBar
                width="120"
                value={sort}
                onChange={e => {
                  setSort(e.target.value);
                  setPage(0); // 카테고리 변경 시, 페이지를 1로 설정 (0-based index이기 때문에 0)
                  setSearchParams({
                    search,
                    page: 1, // 카테고리 변경 시, 페이지를 1로 설정
                    size,
                    category,
                    sort: e.target.value,
                  });
                }}
                items={sortList}
              ></SelectBar>
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
                ISBN
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                카테고리
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                수량
              </th>
              <th
                scope="col"
                className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              ></th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {bookInfoList &&
              bookInfoList.map((book, index) => (
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
              maxWidth: '70vw',
              height: 'fit-content',
              maxHeight: '100vh',
              overflow: 'auto',
            }}
          >
            {/* 모달 왼쪽 내용 */}
            <div className="flex justify-center items-center mb-6 mx-12">
              <h1 className="text-2xl font-bold">{bookStockList.title}</h1>
            </div>
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
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
                {bookStockList.bookList &&
                  bookStockList.bookList.map((book, index) => (
                    <tr key={index}>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
                        {book.callSign}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
                        {book.isAvailable ? '대출 중' : '대출 가능'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
                        {book.status === 'FINE' ? '양호' : '파손'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
                        {book.createdAt}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-black-500">
                        {book.updatedAt}
                      </td>
                      <td className="py-4 whitespace-nowrap text-sm text-black-500">
                        <DefaultButton
                          color={'red'}
                          size="small"
                          onClick={() => handleDeleteClick(book.id)}
                        >
                          삭제
                        </DefaultButton>
                      </td>
                      <td className="py-4 whitespace-nowrap text-sm text-black-500">
                        <DefaultButton
                          color={'blue'}
                          size="small"
                          onClick={() =>
                            handleEditClick(bookStockList, book.id)
                          }
                        >
                          수정
                        </DefaultButton>
                      </td>
                    </tr>
                  ))}
              </tbody>
            </table>
            <div className="mt-4 flex justify-end">
              <DefaultButton onClick={toggleModal}>닫기</DefaultButton>
            </div>
          </div>
          {isEdit ? (
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
                <h1 className="text-2xl font-bold"></h1>
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
                      value={editBook.title}
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
                      value={editBook.author}
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
                      value={editBook.publisher}
                      onChange={handleInputChange}
                    />
                    <label
                      htmlFor="category"
                      className="block text-sm font-medium text-gray-700 mt-3"
                    >
                      카테고리
                    </label>
                    <select
                      id="categoryName"
                      name="category"
                      className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md text-gray-500"
                      value={editBook.category}
                      onChange={handleInputChange}
                    >
                      {categoryList.map((category, index) => (
                        <option key={index} value={category.categoryName}>
                          {category.categoryName}
                        </option>
                      ))}
                    </select>

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
                      value={editBook.isbn}
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
                      id="status"
                      name="status"
                      className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md text-gray-500"
                      value={editBook.status}
                      onChange={handleInputChange}
                    >
                      <option value="FINE">보통</option>
                      <option value="BREAK">파손</option>
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
                      value={editBook.image}
                      onChange={handleInputChange}
                    />
                  </div>
                </div>
                {/* 버튼 */}
                <div className="mt-4 flex justify-end">
                  <DefaultButton onClick={toggleEditCloseModal} color={'red'}>
                    취소
                  </DefaultButton>

                  <DefaultButton onClick={onSubmit} color={'blue'}>
                    수정
                  </DefaultButton>
                </div>
              </div>
            </div>
          ) : (
            <div></div>
          )}
        </div>
      )}
    </>
  );
}
