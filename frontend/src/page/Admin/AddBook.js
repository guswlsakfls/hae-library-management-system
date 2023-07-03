import DefaultButton from '../../component/common/DefaultButton';
import InputBar from '../../component/common/InputBar';
import { useEffect, useState } from 'react';
import { addBookByIsbnApi, postAddBook } from '../../api/BookApi';
import PostInputBar from '../../component/common/PostInputBar';
import SelectBar from '../../component/common/SelectBar';

const statusText = {
  FINE: '양호',
  BREAK: '손상',
  LOST: '분실',
};

const categoryList = [
  '카테고리 선택',
  '총류',
  '철학',
  '종교',
  '자연과학',
  '기술과학',
  '예술',
  '언어',
  '문학',
  '역사',
];

const bookStatus = ['카테고리 선택', 'FINE', 'BREAK'];

export default function AddBook() {
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
  const [status, setStatus] = useState('');

  // ISBN을 가지고 도서 정보를 요청하는 함수
  const handleSearch = async isbn => {
    try {
      const res = await addBookByIsbnApi(isbn);
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
            const callSignWithoutLastChar = res.data.bookList[0].callSign.slice(
              0,
              -1
            );
            return callSignWithoutLastChar;
          } else {
            return ''; // or any default value
          }
        }
        return res.data.callSign + '.c';
      });

      setDonator(res.data.donator ? res.data.donator : '');
      setBookList(res.data.bookList);
      setStatus(res.data.status);

      console.log(res.data);
    } catch (err) {
      alert(err.response.data.message);
      console.log(err.response.data);
      let errors = err.response.data.errors;
      if (!errors) {
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

  return (
    <main className="flex-grow h-screen overflow-y-scroll">
      <div className="flex justify-center items-center my-10 mx-48">
        <h1 className="text-4xl font-bold">도서 추가</h1>
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
                            book.isAvailable === '대출 가능'
                              ? 'text-blue-500'
                              : 'text-red-500'
                          }`}
                        >
                          {book.isAvailable} {/* 대출상태 */}
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
              items={categoryList}
            ></SelectBar>
          </div>
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
        </div>
      </div>
      <div className="flex justify-center items-center my-10 mx-48">
        <DefaultButton size="large" click={handleAddBook}>
          도서 추가
        </DefaultButton>
      </div>
    </main>
  );
}
