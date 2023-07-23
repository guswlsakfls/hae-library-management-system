import DefaultButton from '../../component/common/DefaultButton';
import { useEffect, useState } from 'react';
import { getMyInfoApi } from '../../api/MemberApi';
import {
  updateNewPasswordApi,
  putMemberWidthdrawalApi,
} from '../../api/MemberApi';

export default function MyInfo() {
  const [modifyIsOpen, modifySetIsOpen] = useState(false);
  const [myInfo, setMyInfo] = useState({});
  const [nowPassword, setNowPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [newPasswordCheck, setNewPasswordCheck] = useState('');

  const modifyToggleModal = () => {
    modifySetIsOpen(!modifyIsOpen);
  };

  const logOut = () => {
    if (window.confirm('로그아웃 하시겠습니까?')) {
      localStorage.removeItem('accessToken');
      window.location.href = '/';
    }
  };

  const onChangeNowPassword = e => {
    setNowPassword(e.target.value);
  };

  const onChangeNewPassword = e => {
    setNewPassword(e.target.value);
  };

  const onChangeNewPasswordCheck = e => {
    setNewPasswordCheck(e.target.value);
  };

  const updateNewPassword = () => {
    if (newPassword !== newPasswordCheck) {
      alert('새 비밀번호가 일치하지 않습니다.');
      return;
    }
    updateNewPasswordApi(nowPassword, newPassword)
      .then(res => {
        alert('비밀번호가 변경되었습니다.');
        modifyToggleModal();
      })
      .catch(err => {
        alert(err.response.data.message);
      });
  };

  const memberWidthdrawal = () => {
    if (window.confirm('회원탈퇴 하시겠습니까?')) {
      putMemberWidthdrawalApi()
        .then(res => {
          alert('회원탈퇴가 완료되었습니다.');
          localStorage.removeItem('accessToken');
          window.location.href = '/';
        })
        .catch(err => {
          alert(err.response.data.message);
        });
    }
  };

  useEffect(() => {
    getMyInfoApi()
      .then(res => {
        setMyInfo(res.data);
      })
      .catch(err => {
        alert('로그인이 필요합니다.');
        window.location.href = '/login';
      });
  }, []);

  return (
    <>
      <div className="flex justify-center items-center my-10 sm:mx-32 lg:mx-96 border-2">
        <div className="m-6 border-b border-gray-100">
          <dl className="divide-y divide-gray-100">
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                이메일 :
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-500 sm:col-span-2 sm:mt-0">
                {myInfo.email}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                대출권수 :
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-500 sm:col-span-2 sm:mt-0">
                {myInfo.lendingCount === 0
                  ? '-'
                  : myInfo.lendingCount + ' / 3 권'}
              </dd>
            </div>
            <div className="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
              <dt className="text-sm font-medium leading-6 text-gray-900">
                연체현황 :
              </dt>
              <dd className="mt-1 text-sm leading-6 text-gray-500 sm:col-span-2 sm:mt-0">
                {myInfo.penaltyEndDate &&
                !isNaN(Date.parse(myInfo.penaltyEndDate))
                  ? new Date(
                      new Date(myInfo.penaltyEndDate).getTime() +
                        24 * 60 * 60 * 1000
                    )
                      .toISOString()
                      .split('T')[0]
                  : '-'}
              </dd>
            </div>
          </dl>
        </div>
      </div>
      <div className="flex justify-center items-center my-10 mx-48">
        <DefaultButton size="large" onClick={memberWidthdrawal}>
          탈퇴하기
        </DefaultButton>
        <DefaultButton color="blue" size="large" onClick={modifyToggleModal}>
          비밀번호 변경
        </DefaultButton>
        <DefaultButton color="red" size="large" onClick={logOut}>
          로그 아웃
        </DefaultButton>
      </div>
      {/*  멤버 수정 모달 창  */}
      {modifyIsOpen && (
        <div className="fixed top-0 left-0 right-0 bottom-0 flex items-center justify-center z-50 bg-gray-500 bg-opacity-50">
          <div
            className="bg-white rounded-lg shadow-xl p-6 border border-gray-200 dark:border-gray-500"
            style={{
              width: 'fit-content',
              maxWidth: '80vw',
              height: 'fit-content',
              maxHeight: '80vh',
              overflow: 'auto',
            }}
          >
            {/* 모달 내용 */}
            <div className="flex justify-center items-center my-5 mx-48">
              <h1 className="text-2xl font-bold">비밀번호 변경</h1>
            </div>
            <div className="flex flex-col">
              <div className="flex flex-row justify-between">
                <div className="flex flex-col">
                  <label
                    htmlFor="nowPassword"
                    className="block text-sm font-medium text-gray-700 mt-10"
                  >
                    현재 비밀번호
                  </label>
                  <input
                    type="password" // 타입을 "password"로 변경했습니다.
                    name="nowPassword"
                    id="nowPassword"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mb-5 mr-96"
                    placeholder="현재 비밀번호를 입력하세요."
                    value={nowPassword}
                    onChange={onChangeNowPassword}
                  />

                  <label
                    htmlFor="email"
                    className="block text-sm font-medium text-gray-700 mt-5"
                  >
                    새로운 비밀번호
                  </label>
                  <input
                    type="password" // 타입을 "password"로 변경했습니다.
                    name="newPassword"
                    id="newPassword"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mb-5 mr-96"
                    placeholder="새로운 비밀번호를 입력하세요."
                    value={newPassword}
                    onChange={onChangeNewPassword}
                  />

                  <label
                    htmlFor="reNewPassword"
                    className="block text-sm font-medium text-gray-700 mt-5"
                  >
                    새로운 비밀번호 확인
                  </label>
                  <input
                    type="password" // 타입을 "password"로 변경했습니다.
                    name="newPasswordCheck"
                    id="newPasswordCheck"
                    className="mt-1 focus:ring-blue-500 focus:border-blue-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md mb-5"
                    placeholder="새로운 비밀번호를 재입력하세요."
                    value={newPasswordCheck}
                    onChange={onChangeNewPasswordCheck}
                  />
                </div>
              </div>
              {/* 버튼 */}
              <div className="mt-4 flex justify-end">
                <DefaultButton
                  color="red"
                  size="large"
                  click={modifyToggleModal}
                >
                  취소
                </DefaultButton>
                <DefaultButton
                  color="blue"
                  size="large"
                  click={updateNewPassword}
                >
                  변경
                </DefaultButton>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
