import axios from 'axios';

const serverIp = 'http://localhost:8080/api';
const accessToken = localStorage.getItem('accessToken');

const getMemberListApi = async (search, page, size, role, sort) => {
  const res = await axios.get(serverIp + '/admin/memberinfo/all', {
    params: {
      search: search,
      page: page === null ? 0 : page,
      size: size === null ? 10 : size,
      role: role === null ? '전체' : role,
      sort: sort === null ? '최신순' : sort,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

const getUserByEmailApi = async email => {
  const res = await axios.post(
    serverIp + '/admin/memberinfo',
    { email: email },
    { headers: { authorization: `Bearer ${accessToken}` } }
  );
  return res.data;
};

const memberSignupApi = async (email, password, checkPassword) => {
  const res = await axios.post(serverIp + '/signup', {
    email: email,
    password: password,
    checkPassword: checkPassword,
  });
  return res.data;
};

const memberLoginApi = async (email, password) => {
  const res = await axios.post(serverIp + '/auth', {
    email: email,
    password: password,
  });
  return res.data;
};

const updateMemberApi = async editMember => {
  console.log(editMember);

  const res = await axios.put(
    serverIp + `/admin/member/update`,
    {
      id: editMember.id,
      email: editMember.email,
      role: editMember.role,
      penaltyEndDate: editMember.penaltyEndDate,
      activated: editMember.activated,
    },
    {
      headers: { authorization: `Bearer ${accessToken}` },
    }
  );
  return res.data;
};

const getMyInfoApi = async () => {
  const res = await axios.get(serverIp + '/member/memberinfo/me', {
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

const updateNewPasswordApi = async (nowPassword, newPassword) => {
  const res = await axios.put(
    serverIp + '/member/changePassword',
    {
      nowPassword: nowPassword,
      newPassword: newPassword,
    },
    {
      headers: { authorization: `Bearer ${accessToken}` },
    }
  );
  return res.data;
};

const putMemberWidthdrawalApi = async () => {
  const res = await axios.put(serverIp + '/member/withdrawal/me', null, {
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

export {
  getUserByEmailApi,
  memberSignupApi,
  memberLoginApi,
  getMemberListApi,
  updateMemberApi,
  getMyInfoApi,
  updateNewPasswordApi,
  putMemberWidthdrawalApi,
};
