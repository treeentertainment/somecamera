// pages/config/layout.js
import React from 'react';
import Layout from '@theme-original/Layout';
import DocSidebar from '@theme/DocSidebar';
import {useDocsSidebar} from '@docusaurus/theme-common/internal';

export default function CustomLayout(props) {
  // 현재 locale/route 에 맞는 sidebar 가져오기
  const sidebar = useDocsSidebar();

  return (
    <Layout {...props}>
      <div style={{ display: 'flex', minHeight: '100vh' }}>
        {/* 사이드바 */}
        {sidebar && (
          <aside
            style={{
              width: '250px',
              borderRight: '1px solid #ddd',
              padding: '1rem',
              background: '#f9f9f9',
            }}
          >
            <DocSidebar sidebar={sidebar} path="/" />
          </aside>
        )}

        {/* 본문 */}
        <main style={{ flex: 1, padding: '2rem' }}>
          {props.children}
        </main>
      </div>
    </Layout>
  );
}
