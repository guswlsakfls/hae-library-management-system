import DefaultButton from '../../component/common/DefaultButton';
import InputBar from '../../component/common/InputBar';
import { useEffect, useState } from 'react';
import {
  addBookByIsbnApi,
  postAddBook,
  postRequestBookApi,
} from '../../api/BookApi';
import PostInputBar from '../../component/common/PostInputBar';
import SelectBar from '../../component/common/SelectBar';
import { getCategoryListApi } from '../../api/CategoryApi';

const statusText = {
  FINE: '양호',
  BREAK: '손상',
  LOST: '분실',
};

const bookStatus = ['FINE', 'BREAK', 'LOST'];

export default function AddBook() {
  const categorySet = {
    총류: '000',
    철학: '100',
    종교: '200',
    사회과학: '300',
    순수과학: '400',
    기술과학: '500',
    예술: '600',
    언어: '700',
    문학: '800',
    역사: '900',
  };

  const [bookList, setBookList] = useState([]);

  const [title, setTitle] = useState('');
  const [image, setImage] = useState('');
  const [author, setAuthor] = useState('');
  const [publisher, setPublisher] = useState('');
  const [publishedAt, setPublishedAt] = useState('');
  const [isbn, setIsbn] = useState(null);
  const [category, setCategory] = useState('');
  const [callSign, setCallSign] = useState('');
  const [donator, setDonator] = useState('');
  const [status, setStatus] = useState('FINE');
  const [categoryList, setCategoryList] = useState([]);
  const [transactionType, setTransactionType] = useState('add'); // 'add', 'order'

  // ISBN을 가지고 도서 정보를 요청하는 함수
  const handleSearch = async isbn => {
    try {
      await addBookByIsbnApi(isbn).then(res => {
        console.log(res.data);
        setTitle(res.data.title);
        setImage(res.data.image);
        setAuthor(res.data.author);
        setPublisher(res.data.publisher);
        setPublishedAt(res.data.publishedAt);
        setIsbn(res.data.isbn);
        setCategory(res.data.category);
        setCallSign(() => {
          if (res.data.callSign === null) {
            if (res.data.bookList.length > 0) {
              const callSignWithoutLastChar =
                res.data.bookList[0].callSign.slice(0, -1);
              return callSignWithoutLastChar;
            } else {
              return (
                categorySet[res.data.category] + '.' + isbn.slice(-3) + '.c'
              );
            }
          }
          return res.data.callSign + '.c';
        });
        setDonator(res.data.donator ? res.data.donator : '');
        setBookList(res.data.bookList);
        setStatus('FINE');
      });
    } catch (err) {
      alert(err.response.data.message);
      let errors = err.response.data.errors;
      if (!errors) {
        setTitle('');
        setImage('');
        setAuthor('');
        setPublisher('');
        setPublishedAt('');
        setIsbn('');
        setCategory('');
        setCallSign('');
        setDonator('');
        setBookList([]);
        setStatus('FINE');
        return;
      }
      let errorMessages = errors
        .map((error, index) => `${index + 1}. ${error.message}`)
        .join('\n\n');
      alert(errorMessages);
    }
  };

  const handleAddBook = () => {
    postAddBook(
      title,
      image,
      author,
      publisher,
      publishedAt,
      isbn,
      category,
      callSign,
      donator,
      status
    )
      .then(res => {
        alert(res.message);
        window.location.reload();
      })
      .catch(err => {
        alert(err.response.data.message);
        setTitle('');
        setImage('');
        setAuthor('');
        setPublisher('');
        setPublishedAt('');
        setIsbn('');
        setCategory('');
        setCallSign('');
        setDonator('');
        setBookList([]);
        setStatus('');

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

  const handleRequestBook = () => {
    postRequestBookApi(
      title,
      image,
      author,
      publisher,
      publishedAt,
      isbn,
      category
    )
      .then(res => {
        alert(res.message);
        window.location.reload();
      })
      .catch(err => {
        alert(err.response.data.message);
        setTitle('');
        setImage('');
        setAuthor('');
        setPublisher('');
        setPublishedAt('');
        setIsbn('');
        setBookList([]);

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
    getCategoryListApi()
      .then(res => {
        setCategoryList(res.data);
      })
      .catch(err => {
        alert('카테고리 목록을 불러오는데 실패했습니다.');
      });
  }, []);

  return (
    <main className="flex-grow h-screen overflow-y-scroll">
      <div className="flex justify-center items-center my-10 mx-48">
        <h1 className="text-4xl font-bold">도서 추가 / 구매 요청</h1>
      </div>
      <div className="flex justify-center items-center my-10 mx-48 text-2xl">
        <select
          value={transactionType}
          onChange={e => {
            setTransactionType(e.target.value);
            setTitle('');
            setImage('');
            setAuthor('');
            setPublisher('');
            setPublishedAt('');
            setIsbn('');
            setCategory('');
            setCallSign('');
            setDonator('');
            setBookList([]);
            setStatus('');
          }}
        >
          <option value="add">도서 추가</option>
          <option value="order">도서 구매 요청</option>
        </select>
      </div>
      <div className="flex justify-center items-center mt-10 mx-48">
        <h1 className="text-2xl font-bold mr-6">도서 검색</h1>
        <PostInputBar
          text="ISBN을 입력해 주세요."
          onSubmit={handleSearch}
        ></PostInputBar>
      </div>

      <div className="grid w-full grid-cols-1 items-start gap-x-6 gap-y-8 sm:grid-cols-12 lg:gap-x-8 px-32 py-8 mt-6 border-t border-b">
        <div className="aspect-h-3 aspect-w-2 overflow-hidden rounded-lg bg-gray-100 sm:col-span-4 lg:col-span-5 border">
          <img src={image} className="object-cover object-center" />
        </div>

        <div className="sm:col-span-8 lg:col-span-7">
          <h1 className="text-2xl font-bold text-gray-900 sm:pr-12 border-b pb-2">
            도서 정보
          </h1>

          <section aria-labelledby="options-heading" className="mt-5">
            <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                제목
              </dt>
              <InputBar
                value={title}
                onChange={e => setTitle(e.target.value)}
              />
            </div>
            <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                저자
              </dt>
              <InputBar
                value={author}
                onChange={e => setAuthor(e.target.value)}
              />
            </div>
            <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                출판사
              </dt>
              <InputBar
                value={publisher}
                onChange={e => setPublisher(e.target.value)}
              />
            </div>
            <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                발행일
              </dt>
              <InputBar
                value={publishedAt}
                onChange={e => setPublishedAt(e.target.value)}
              />
            </div>
            <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                ISBN
              </dt>
              <InputBar value={isbn} onChange={e => setIsbn(e.target.value)} />
            </div>

            <div className="mt-10">
              <div className="flex items-center justify-between border-b">
                <h4 className="text-2xl font-medium text-gray-900">보유도서</h4>
              </div>
              <div style={{ maxHeight: '165px', overflowY: 'scroll' }}>
                {bookList ? (
                  bookList.map((book, index) => (
                    <div className="mt-6" key={index}>
                      <div className="flex items-center">
                        <h4 className="text-sm font-medium text-gray-900 mr-5">
                          {book.callSign} {/* 청구기호 */}
                        </h4>
                        <h4
                          className={`text-sm font-medium mr-5 ${
                            book.isAvailable === false
                              ? 'text-blue-500'
                              : 'text-red-500'
                          }`}
                        >
                          {book.isAvailable ? '대출 중' : '대출 가능'}{' '}
                          {/* 대출상태 */}
                        </h4>
                        <h4
                          className={`text-sm font-medium mr-5 ${
                            book.status === 'FINE'
                              ? 'text-blue-500'
                              : 'text-red-500'
                          }`}
                        >
                          {statusText[book.status]} {/* 대출상태 */}
                        </h4>
                      </div>
                    </div>
                  ))
                ) : (
                  <div>보유 도서가 없습니다.</div>
                )}
              </div>
            </div>
          </section>
        </div>
        <div className="sm:col-span-8 lg:col-span-7">
          <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
            <dt className="text-xl font-medium leading-6 text-gray-900">
              카테고리
            </dt>
            <SelectBar
              value={category}
              onChange={e => setCategory(e.target.value)}
              items={categoryList.map(cat => cat.categoryName)}
            ></SelectBar>
          </div>
          {transactionType === 'add' ? (
            <>
              <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
                <dt className="text-xl font-medium leading-6 text-gray-900">
                  도서 상태
                </dt>
                <SelectBar
                  value={status}
                  onChange={e => setStatus(e.target.value)}
                  items={bookStatus}
                ></SelectBar>
              </div>
              <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
                <dt className="text-xl font-medium leading-6 text-gray-900">
                  청구기호
                </dt>
                <InputBar
                  value={callSign}
                  onChange={e => setCallSign(e.target.value)}
                />
              </div>
              <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
                <dt className="text-xl font-medium leading-6 text-gray-900">
                  기부자
                </dt>
                <InputBar
                  value={donator}
                  onChange={e => setDonator(e.target.value)}
                />
              </div>
            </>
          ) : null}
        </div>
      </div>
      {transactionType === 'add' ? (
        <div className="flex justify-center items-center my-10 mx-48">
          <DefaultButton color={'blue'} size="large" click={handleAddBook}>
            도서 추가
          </DefaultButton>
        </div>
      ) : (
        <div className="flex justify-center items-center my-10 mx-48">
          <DefaultButton color={'blue'} size="large" click={handleRequestBook}>
            도서 구매 요청
          </DefaultButton>
        </div>
      )}
    </main>
  );
}
