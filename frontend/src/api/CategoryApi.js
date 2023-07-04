import axios from 'axios';

const serverIp = 'http://localhost:8080/api';
const accessToken = localStorage.getItem('accessToken');

const createCategoryApi = async name => {
  console.log(name);
  const res = await axios.post(
    serverIp + '/admin/category/create',
    {
      categoryName: name,
    },
    { headers: { authorization: `Bearer ${accessToken}` } }
  );
  return res.data;
};

const getCategoryListApi = async () => {
  const res = await axios.get(serverIp + '/admin/category/all', {
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

const updateCategoryApi = async (id, name) => {
  const res = await axios.put(
    serverIp + '/admin/category/update',
    {
      categoryId: id,
      updatedCategoryName: name,
    },
    { headers: { authorization: `Bearer ${accessToken}` } }
  );
  return res.data;
};

const deleteCategoryApi = async id => {
  const res = await axios.delete(serverIp + `/admin/category/${id}/delete`, {
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
