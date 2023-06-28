import DefaultButton from '../../component/common/DefaultButton';
import SearchBar from '../../component/common/SearchBar';
import InputBar from '../../component/common/InputBar';

const user = {
  email: 'guswlsakfls@gmail.com',
  lendingCount: 2,
  penaltyDate: '2021-10-10',
};

const book = {
  title: 'Regional Paradigm Technician',
  callSign: '123.12.v1.c1',
  returningDate: '2012-20-23',
};

const product = {
  name: 'Basic Tee 6-Pack ',
  price: '$192',
  rating: 3.9,
  reviewCount: 117,
  href: '#',
  imageSrc:
    'https://tailwindui.com/img/ecommerce-images/product-quick-preview-02-detail.jpg',
  imageAlt: 'Two each of gray, white, and black shirts arranged on table.',
  colors: [
    { name: 'White', class: 'bg-white', selectedClass: 'ring-gray-400' },
    { name: 'Gray', class: 'bg-gray-200', selectedClass: 'ring-gray-400' },
    { name: 'Black', class: 'bg-gray-900', selectedClass: 'ring-gray-900' },
  ],
  sizes: [
    { name: 'XXS', inStock: true },
    { name: 'XS', inStock: true },
    { name: 'S', inStock: true },
    { name: 'M', inStock: true },
    { name: 'L', inStock: true },
    { name: 'XL', inStock: true },
    { name: 'XXL', inStock: true },
    { name: 'XXXL', inStock: false },
  ],
};

export default function AddBook() {
  return (
    <main className="flex-grow h-screen overflow-y-scroll">
      <div className="flex justify-center items-center my-10 mx-48">
        <h1 className="text-4xl font-bold">도서 추가</h1>
      </div>
      <div className="flex justify-center items-center mt-10 mx-48">
        <h1 className="text-2xl font-bold mr-6">도서 검색</h1>
        <SearchBar text="이메일을 입력해 주세요."></SearchBar>
      </div>

      <div className="grid w-full grid-cols-1 items-start gap-x-6 gap-y-8 sm:grid-cols-12 lg:gap-x-8 px-32 py-8 mt-6 border-t border-b">
        <div className="aspect-h-3 aspect-w-2 overflow-hidden rounded-lg bg-gray-100 sm:col-span-4 lg:col-span-5">
          <img src={product.imageSrc} className="object-cover object-center" />
        </div>
        <div className="sm:col-span-8 lg:col-span-7">
          <h1 className="text-5xl font-bold text-gray-900 sm:pr-12 border-b pb-2">
            {product.name}
          </h1>

          <section aria-labelledby="options-heading" className="mt-5">
            <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                저자
              </dt>
              <InputBar />
            </div>
            <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                출판사
              </dt>
              <InputBar />
            </div>
            <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                발행일
              </dt>
              <InputBar />
            </div>
            <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-xl font-medium leading-6 text-gray-900">
                ISBN
              </dt>
              <InputBar />
            </div>

            {/* Sizes */}
            <div className="mt-10">
              <div className="flex items-center justify-between border-b">
                <h4 className="text-2xl font-medium text-gray-900">보유도서</h4>
              </div>
              {/* 테이블로 청구기호 대출상태 나타대기 */}
              <div className="mt-6">
                <div className="flex items-center">
                  <h4 className="text-sm font-medium text-gray-900 mr-5">
                    청구기호
                  </h4>
                  <h4 className="text-sm font-medium text-gray-900">
                    대출상태
                  </h4>
                </div>
              </div>
              <div className="mt-6">
                <div className="flex items-center">
                  <h4 className="text-sm font-medium text-gray-900 mr-5">
                    청구기호
                  </h4>
                  <h4 className="text-sm font-medium text-gray-900">
                    대출상태
                  </h4>
                </div>
              </div>
            </div>
          </section>
        </div>
        <div className="sm:col-span-8 lg:col-span-7">
          <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
            <dt className="text-xl font-medium leading-6 text-gray-900">
              카테고리
            </dt>
            <InputBar />
          </div>
          <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
            <dt className="text-xl font-medium leading-6 text-gray-900">
              청구기호
            </dt>
            <InputBar />
          </div>
          <div className="py-3 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
            <dt className="text-xl font-medium leading-6 text-gray-900">
              기부자
            </dt>
            <InputBar />
          </div>
        </div>
      </div>
      <div className="flex justify-center items-center my-10 mx-48">
        <DefaultButton size="large">도서 추가</DefaultButton>
      </div>
    </main>
  );
}
