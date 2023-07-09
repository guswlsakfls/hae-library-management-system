import React, { useEffect, useState } from 'react';
import { MemberContext } from './MemberContext';

export const MemberProvider = ({ children }) => {
  const [memberInfo, setMemberInfo] = useState(null);

  useEffect(() => {
    const memberInfo = JSON.parse(localStorage.getItem('memberInfo'));
    // console.log('memberInfo in MemberProvider:', memberInfo.role);
    if (memberInfo) {
      setMemberInfo(memberInfo);
    }
  }, []);

  return (
    <MemberContext.Provider value={{ memberInfo, setMemberInfo }}>
      {children}
    </MemberContext.Provider>
  );
};
