import path from 'path';
import remarkGithubAdmonitionsToDirectives from 'remark-github-admonitions-to-directives';

export default {
  title: 'SomeCamera',
  url: 'https://camera.treeentertainment.tech',
  baseUrl: '/',
  favicon: 'favicon.ico',

  onBrokenLinks: 'warn',
  onBrokenMarkdownLinks: 'warn',

  i18n: {
    defaultLocale: 'en',
    locales: ['en', 'kr'],
  },

  markdown: {
    mermaid: true,
  },

  themes: ['@docusaurus/theme-mermaid'],

  // Classic 프리셋에서 docs와 blog 설정을 명시적으로 구성
  presets: [
    [
      'classic',
      {
        docs: false, // 별도 플러그인으로 관리하므로 비활성화
        blog: false, // 블로그 기능 비활성화
        pages: {
          // pages 플러그인 명시적 활성화
          path: 'pages',
          routeBasePath: '/',
        },
      },
    ],
  ],

  plugins: [
    [
      '@docusaurus/plugin-client-redirects',
      {
        redirects: [
          { from: '/download', to: 'https://github.com/treeentertainment/somecamera/releases/latest' },
        ],
      },
    ],
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'en',
        path: 'pages/docs/en',
        routeBasePath: 'docs/en',
        sidebarPath: path.resolve('./pages/config/sidebars/sidebars_en.js'),
        remarkPlugins: [remarkGithubAdmonitionsToDirectives],
      },
    ],
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'kr',
        path: 'pages/docs/kr',
        routeBasePath: 'docs/kr',
        sidebarPath: path.resolve('./pages/config/sidebars/sidebars_kr.js'),
        remarkPlugins: [remarkGithubAdmonitionsToDirectives],
      },
    ],
  ],

  // configureWebpack을 올바른 위치로 이동
  configureWebpack: () => ({
    resolve: {
      alias: {
        '@site/src/pages': path.resolve('./pages'),
        '@site/pages': path.resolve('./pages'),
      },
    },
  }),

  themeConfig: {
    navbar: {
      title: 'SomeCamera',
      items: [
        { to: '/docs/en/intro', label: 'Docs (EN)', position: 'left' },
        { to: '/docs/kr/intro', label: 'Docs (KR)', position: 'left' },
        {
          href: 'https://github.com/treeentertainment/SomeCamera',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Documentation',
          items: [
            { label: 'Docs (EN)', to: '/docs/en/intro' },
            { label: 'Docs (KR)', to: '/docs/kr/intro' },
          ],
        },
        {
          title: 'Community',
          items: [
            { label: 'GitHub', href: 'https://github.com/treeentertainment/SomeCamera' },
            { label: 'Releases', href: 'https://github.com/treeentertainment/somecamera/releases/latest' },
          ],
        },
      ],
    },
    // Mermaid 테마 설정 추가
    mermaid: {
      theme: { light: 'neutral', dark: 'dark' },
    },
  },
};
