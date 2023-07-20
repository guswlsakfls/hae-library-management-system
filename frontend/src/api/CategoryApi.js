import axios from 'axios';

const serverIp = 'http://localhost:8080/api';
const accessToken = localStorage.getItem('accessToken');

const createCategoryApi = async name => {
  const res = await axios.post(
    serverIp + '/admin/category',
    {
      categoryName: name,
    },
    { headers: { authorization: `Bearer ${accessToken}` } }
  );
  return res.data;
};

const getCategoryListApi = async () => {
  const res = await axios.get(serverIp + '/category/all', {
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

const updateCategoryApi = async (id, name) => {
  const res = await axios.put(
    serverIp + '/admin/category',
    {
      categoryId: id,
      updatedCategoryName: name,
    },
    { headers: { authorization: `Bearer ${accessToken}` } }
  );
  return res.data;
};

const deleteCategoryApi = async id => {
  const res = await axios.delete(serverIp + `/admin/category/${id}`, {
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

export {
  getCategoryListApi,
  createCategoryApi,
  updateCategoryApi,
  deleteCategoryApi,
};
