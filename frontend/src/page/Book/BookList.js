import Dropdown from '../../component/common/Dropdown';
import SearchBar from '../../component/common/SearchBar';
import { useState } from 'react';
import Footer from '../../component/Footer';
import Pagination from '../../component/common/Pagination';

/*
  This example requires some changes to your config:
  
  ```
  // tailwind.config.js
  module.exports = {
    // ...
    plugins: [
      // ...
      require('@tailwindcss/forms'),
    ],
  }
  ```
*/
const products = [
  {
    id: 1,
    name: 'Throwback Hip Bag',
    href: '#',
    color: 'Salmon',
    price: '$90.00',
    quantity: 1,
    imageSrc:
      'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-01.jpg',
    imageAlt:
      'Salmon orange fabric pouch with match zipper, gray zipper pull, and adjustable hip belt.',
  },
  {
    id: 2,
    name: 'Medium Stuff Satchel',
    href: '#',
    color: 'Blue',
    price: '$32.00',
    quantity: 1,
    imageSrc:
      'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-02.jpg',
    imageAlt:
      'Front of satchel with blue canvas body, black straps and handle, drawstring top, and front zipper pouch.',
  },
  {
    id: 2,
    name: 'Medium Stuff Satchel',
    href: '#',
    color: 'Blue',
    price: '$32.00',
    quantity: 1,
    imageSrc:
      'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-02.jpg',
    imageAlt:
      'Front of satchel with blue canvas body, black straps and handle, drawstring top, and front zipper pouch.',
  },
  {
    id: 2,
    name: 'Medium Stuff Satchel',
    href: '#',
    color: 'Blue',
    price: '$32.00',
    quantity: 1,
    imageSrc:
      'https://tailwindui.com/img/ecommerce-images/shopping-cart-page-04-product-02.jpg',
    imageAlt:
      'Front of satchel with blue canvas body, black straps and handle, drawstring top, and front zipper pouch.',
  },
  // More products...
];

export default function BookList() {
  const [open, setOpen] = useState(true);

  return (
    <>
      {/*
        This example requires updating your template:

        ```
        <html class="h-full bg-white">
        <body class="h-full">
        ```
      */}
      <div className="sm:mx-auto sm:w-full border-b-2">
        <h2 className="mt-10 pb-10 text-center text-4xl font-bold leading-9 tracking-tight text-gray-900">
          도서목록
        </h2>
      </div>
      <div className="flex flex-1 justify-center px-6 lg:px-8">
        <div className="mt-5 pb-3 sm:mx-auto sm:w-full sm:max-w-sm">
          <SearchBar />
          <div className="mt-12 pb-1 flex items-center">
            <h1 className="py-auto pr-2 text-lg">도서목록</h1>
            <div className="pr-1">
              <Dropdown option1="전체" />
            </div>
            <div className="pr-1">
              <Dropdown option1="날짜순" />
            </div>
          </div>
        </div>
      </div>
      {/* 리스트 */}
      <div className="flex flex-wrap justify-center lg:px-12 mx-32 border-t-2 border-b-2">
        {products.map(product => (
          <div key={product.id} className="w-full md:w-1/2 px-2">
            <li className="flex py-6 px-8">
              <div className="h-24 w-24 flex-shrink-0 overflow-hidden rounded-md border border-gray-200">
                <img
                  src={product.imageSrc}
                  alt={product.imageAlt}
                  className="h-full w-full object-cover object-center"
                />
              </div>

              <div className="ml-4 flex flex-1 flex-col">
                <div>
                  <div className="flex justify-between text-base font-medium text-gray-900">
                    <h3>
                      <a href={product.href}>{product.name}</a>
                    </h3>
                    <p className="ml-4">{product.price}</p>
                  </div>
                  <p className="mt-1 text-sm text-gray-500">{product.color}</p>
                </div>
                <div className="flex flex-1 items-end justify-between text-sm">
                  <p className="text-gray-500">Qty {product.quantity}</p>

                  <div className="flex">
                    <button
                      type="button"
                      className="font-medium text-gray-400 hover:text-indigo-500"
                    >
                      자세히 보기 >
                    </button>
                  </div>
                </div>
              </div>
            </li>
          </div>
        ))}
      </div>
      <Pagination />
      <Footer />
    </>
  );
}
