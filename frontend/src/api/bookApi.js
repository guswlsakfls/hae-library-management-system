import axios from 'axios';

const serverIp = 'http://localhost:8080/api';
const accessToken = localStorage.getItem('accessToken');

const postAddBook = async (
  title,
  image,
  author,
  publisher,
  publishedAt,
  isbn,
  category,
  callSign,
  donator,
  status
) => {
  const res = await axios.post(
    serverIp + '/admin/book/create',
    {
      title: title,
      image: image,
      author: author,
      publisher: publisher,
      publishedAt: publishedAt,
      isbn: isbn,
      categoryName: category,
      callSign: callSign,
      donator: donator,
      status: status,
    },
    {
      headers: { authorization: `Bearer ${accessToken}` },
    }
  );
  return res.data;
};

const postRequestBookApi = async (
  title,
  image,
  author,
  publisher,
  publishedAt,
  isbn,
  category
) => {
  const res = await axios.post(
    serverIp + '/admin/request-book/create',
    {
      title: title,
      image: image,
      author: author,
      publisher: publisher,
      publishedAt: publishedAt,
      isbn: isbn,
      categoryName: category,
    },
    {
      headers: { authorization: `Bearer ${accessToken}` },
    }
  );
  return res.data;
};

// freeBoard 해당 페이지 게시판 리스트 받아오기.
const getBookListApi = async (search, page, size, category, sort) => {
  const res = await axios.get(serverIp + '/bookinfo/all', {
    params: {
      search: search,
      page: page === null ? 0 : page,
      size: size === null ? 10 : size,
      category: category,
      sort: sort,
    },
  });

  return res.data;
};

const getRequestBookListApi = async (
  search,
  page,
  size,
  category,
  sort,
  approved
) => {
  const res = await axios.get(serverIp + '/admin/request-book/all', {
    params: {
      search: search,
      page: page === null ? 0 : page,
      size: size === null ? 10 : size,
      approved: approved,
      category: category,
      sort: sort,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });

  return res.data;
};

const getLendingListApi = async (
  search,
  page,
  size,
  isLendingOrReturning,
  sort
) => {
  const res = await axios.get(serverIp + '/admin/lending/all', {
    params: {
      search: search,
      page: page === null ? 0 : page,
      size: size === null ? 10 : size,
      isLendingOrReturning: isLendingOrReturning,
      sort: sort,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });

  return res.data; // 최신화 위해 역순으로 정렬.
};

const getBookByCallSignApi = async callsign => {
  const res = await axios.get(serverIp + `/admin/book/callsign`, {
    params: {
      callsign: callsign,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

const getBookInfoByIdApi = async id => {
  const res = await axios.get(serverIp + '/bookinfo/' + id);
  return res.data;
};

const addBookByIsbnApi = async isbn => {
  const res = await axios.get(serverIp + `/admin/bookinfo/isbn`, {
    params: {
      isbn: isbn,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

const lendingBookApi = async (bookId, userId, lendingCondition) => {
  const res = await axios.post(
    serverIp + '/admin/lending/create',
    {
      bookId: bookId,
      userId: userId,
      lendingCondition: lendingCondition,
    },
    { headers: { authorization: `Bearer ${accessToken}` } }
  );
  return res.data;
};

const returningBookApi = async (lendingId, returningCondition) => {
  const res = await axios.put(
    serverIp + '/admin/lending/returning',
    {
      lendingId: lendingId,
      returningCondition: returningCondition,
    },
    { headers: { authorization: `Bearer ${accessToken}` } }
  );
  return res.data;
};

const getBookStockListApi = async id => {
  const res = await axios.get(serverIp + `/bookinfo/` + id, {
    headers: { authorization: `Bearer ${accessToken}` },
  });

  return res.data; // 최신화 위해 역순으로 정렬.
};

const updateBookStockApi = async editBook => {
  const res = await axios.put(
    serverIp + '/admin/book/update',
    {
      id: editBook.id,
      title: editBook.title,
      isbn: editBook.isbn,
      author: editBook.author,
      image: editBook.image,
      callSign: editBook.callSign,
      donator: editBook.donator,
      status: editBook.status,
      categoryName: editBook.category,
      publisher: editBook.publisher,
      publishedAt: editBook.publishedAt,
    },
    { headers: { authorization: `Bearer ${accessToken}` } }
  );
  return res.data;
};

const getMeLendingHistoryListApi = async (
  search,
  page,
  size,
  isLendingOrReturning,
  sort
) => {
  const res = await axios.get(serverIp + '/member/lending-history/me', {
    params: {
      search: search,
      page: page === null ? 0 : page,
      size: size === null ? 10 : size,
      isLendingOrReturning: isLendingOrReturning,
      sort: sort,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

const getLendgingInfoApi = async callsign => {
  const res = await axios.get(serverIp + `/admin/lending/callsign`, {
    params: {
      callsign: callsign,
    },
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

const deleteBookApi = async id => {
  const res = await axios.delete(serverIp + `/admin/book/${id}/delete`, {
    headers: { authorization: `Bearer ${accessToken}` },
  });
  return res.data;
};

const deleteRequestBookApi = async id => {
  const res = await axios.delete(
    serverIp + `/admin/request-book/${id}/delete`,
    {
      headers: { authorization: `Bearer ${accessToken}` },
    }
  );
  return res.data;
};

export {
  getBookListApi,
  getBookInfoByIdApi,
  getBookByCallSignApi,
  addBookByIsbnApi,
  postAddBook,
  lendingBookApi,
  returningBookApi,
  getLendingListApi,
  getBookStockListApi,
  updateBookStockApi,
  getMeLendingHistoryListApi,
  getLendgingInfoApi,
  deleteBookApi,
  postRequestBookApi,
  getRequestBookListApi,
  deleteRequestBookApi,
};
