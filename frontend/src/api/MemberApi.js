import axios from 'axios';

const serverIp = 'http://localhost:8080/api';
const accessToken = localStorage.getItem('accessToken');

const getMemberListApi = async (search, page, size) => {
  const res = await axios.get(serverIp + '/admin/memberinfo/all', {
    params: {
      search: search,
      page: page === null ? 0 : page,
      size: size === null ? 10 : size,
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

export {
  getUserByEmailApi,
  memberSignupApi,
  memberLoginApi,
  getMemberListApi,
  updateMemberApi,
  getMyInfoApi,
};
