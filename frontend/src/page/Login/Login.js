import React, { useState } from 'react';
import Footer from '../../component/Footer';
import { memberLoginApi } from '../../api/MemberApi';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = () => {
    memberLoginApi(email, password)
      .then(res => {
        alert(res.message);
        const accessToken = res.data.accessToken;
        // accessToken 파싱
        localStorage.setItem('accessToken', accessToken);

        window.location.href = '/';
      })
      .catch(err => {
        console.log(err);
        if (err.response.status && err.response.status === 404) {
          alert('아이디와 비밀번호가 일치하지 않습니다.');
        } else if (err.response.status && err.response.status === 400) {
          let errors = err.response.data.errors;
          if (!errors) {
            return;
          }
          let errorMessages = errors
            .map((error, index) => `${index + 1}. ${error.message}`)
            .join('\n\n');
          alert(errorMessages);
        } else {
          alert('휴면계정입니다. 관리자에게 문의하세요.');
        }
      });
  };

  return (
    <>
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
            로그인
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <div className="space-y-6">
            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium leading-6 text-gray-900"
              >
                이메일
              </label>
              <div className="mt-2">
                <input
                  id="email"
                  name="email"
                  type="email"
                  placeholder="이메일을 입력해주세요."
                  value={email}
                  onChange={event => setEmail(event.target.value)}
                  className="block w-full rounded-md border-0 px-1.5 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label
                  htmlFor="password"
                  className="block text-sm font-medium leading-6 text-gray-900"
                >
                  비밀번호
                </label>
              </div>
              <div className="mt-2">
                <input
                  id="password"
                  name="password"
                  type="password"
                  placeholder="패스워드를 입력해 주세요."
                  value={password}
                  onChange={event => setPassword(event.target.value)}
                  className="block w-full rounded-md border-0 px-1.5 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div>
              <button
                type="submit"
                className="flex w-full justify-center rounded-md bg-blue-500 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                onClick={handleLogin}
              >
                로그인
              </button>
            </div>
          </div>

          <p className="mt-10 text-center text-sm text-gray-500">
            회원이 아니신가요?{' '}
            <a
              href="/signup"
              className="font-semibold leading-6 text-indigo-600 hover:text-indigo-500"
            >
              회원가입 하러가기!
            </a>
          </p>
        </div>
      </div>
      <Footer />
    </>
  );
}
