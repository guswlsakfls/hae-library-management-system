import axios from 'axios';

const serverIp = 'http://localhost:8080/api';

const getUserByEmail = async email => {
  const res = await axios.post(serverIp + '/memberInfo', {
    email: email,
  });
  return res.data;
};

export { getUserByEmail };
