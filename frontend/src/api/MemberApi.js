import axios from 'axios';

const serverIp = 'http://localhost:8080/api';
const accessToken = localStorage.getItem('accessToken');

const getMemberListApi = async (search, page, size) => {
  const res = await axios.get(serverIp + '/member/all', {
    params: {
      search: search,
      page: page === null ? 0 : page,
      size: size === null ? 10 : size,
    },
  });
  return res.data;
};

const getUserByEmailApi = async email => {
  const res = await axios.post(
    serverIp + '/memberInfo',
    { email: email }
    // { headers: { authorization: `Bearer ${accessToken}` } }
  );
  return res.data;
};

const memberSignupApi = async (email, password, checkPassword) => {
  const res = await axios.post(serverIp + '/member/signup', {
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
  const res = await axios.put(serverIp + `/member/update`, {
    id: editMember.id,
    email: editMember.email,
    role: editMember.role,
    penaltyEndDate: editMember.penaltyEndDate,
    activated: editMember.activated,
  });
  return res.data;
};

export {
  getUserByEmailApi,
  memberSignupApi,
  memberLoginApi,
  getMemberListApi,
  updateMemberApi,
};
