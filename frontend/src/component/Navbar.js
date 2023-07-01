import { Disclosure } from '@headlessui/react';
import { useState } from 'react';
import { useEffect } from 'react';

function classNames(...classes) {
  return classes.filter(Boolean).join(' ');
}

export default function Navbar() {
  // 초기 상태는 '도서목록'
  const [current, setCurrent] = useState('/search');

  const navigation = [
    { name: '관리자페이지', href: '/admin', current: current === '/admin' },
    { name: '안내사항', href: '/info', current: current === '/info' },
    { name: '도서목록', href: '/booklist', current: current === '/booklist' },
    { name: '로그인', href: '/login', current: current === '/login' },
  ];

  useEffect(() => {
    // 페이지가 로드되거나 라우트가 변경될 때마다 현재 위치를 업데이트
    setCurrent(window.location.pathname);
  }, []);

  return (
    <Disclosure as="nav" className="border-b-2 border-black-900">
      {({ open }) => (
        <div className="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
          <div className="relative flex h-16 items-center justify-between">
            <div className="flex flex-1 items-center justify-start sm:items-stretch sm:justify-start">
              <div className="flex flex-shrink-0 items-center">
                <button
                  onClick={() => {
                    window.location.href = '/';
                  }}
                  className="cursor-pointer focus:outline-none"
                >
                  <img
                    className="h-8 w-auto lg:block"
                    src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=500"
                    alt="Your Company"
                  />
                </button>
              </div>
            </div>
            <div className="flex sm:ml-6 sm:block">
              <div className="flex space-x-4">
                {navigation.map(item => (
                  <a
                    key={item.name}
                    href={item.href}
                    className={classNames(
                      item.current
                        ? ' text-blue-500'
                        : 'text-gray-500 hover:text-black',
                      'rounded-md px-3 py-2 text-sm font-medium'
                    )}
                    aria-current={item.current ? 'page' : undefined}
                  >
                    {item.name}
                  </a>
                ))}
              </div>
            </div>
          </div>
        </div>
      )}
    </Disclosure>
  );
}
