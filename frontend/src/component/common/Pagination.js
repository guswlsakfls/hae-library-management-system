import React from 'react';
import styled from 'styled-components';
import Pagin from 'react-js-pagination';

const items = [
  {
    id: 1,
    title: 'Back End Developer',
    department: 'Engineering',
    type: 'Full-time',
    location: 'Remote',
  },
  {
    id: 2,
    title: 'Front End Developer',
    department: 'Engineering',
    type: 'Full-time',
    location: 'Remote',
  },
  {
    id: 3,
    title: 'User Interface Designer',
    department: 'Design',
    type: 'Full-time',
    location: 'Remote',
  },
];

export default function Pagination() {
  return (
    <PaginationBox>
      <Pagin
        activePage={1}
        itemsCountPerPage={5}
        totalItemsCount={300}
        pageRangeDisplayed={5}
        // onChange={handlePageChange}
      ></Pagin>
    </PaginationBox>
  );
}

const PaginationBox = styled.div`
  .pagination {
    display: flex;
    justify-content: center;
    margin-top: 15px;
  }
  ul {
    list-style: none;
    padding: 0;
  }
  ul.pagination li {
    display: inline-block;
    width: 30px;
    height: 30px;
    border: 1px solid #e2e2e2;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 1rem;
  }
  ul.pagination li:first-child {
    border-radius: 5px 0 0 5px;
  }
  ul.pagination li:last-child {
    border-radius: 0 5px 5px 0;
  }
  ul.pagination li a {
    text-decoration: none;
    color: #337ab7;
    font-size: 1rem;
  }
  ul.pagination li.active a {
    color: white;
  }
  ul.pagination li.active {
    background-color: #337ab7;
  }
  ul.pagination li a:hover,
  ul.pagination li a.active {
    color: blue;
  }
`;
