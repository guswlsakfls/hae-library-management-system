import axios from 'axios';

const serverIp = 'http://localhost:8080/api';
const accessToken = localStorage.getItem('accessToken');

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

export { getUserByEmailApi, memberSignupApi, memberLoginApi };
