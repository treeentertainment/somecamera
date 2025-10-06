import path from 'path';
import remarkGithubAdmonitionsToDirectives from 'remark-github-admonitions-to-directives';

export default {
  title: 'SomeCamera',
  url: 'https://camera.treeentertainment.tech',
  baseUrl: '/',
  favicon: 'favicon.ico',

  onBrokenLinks: 'warn',

  i18n: {
    defaultLocale: 'en',
    locales: ['en', 'kr'],
  },

  markdown: {
    mermaid: true,
    hooks: {
      onBrokenMarkdownLinks: 'warn',
    },
  },

  themes: ['@docusaurus/theme-mermaid'],

  presets: [
    [
      'classic',
      {
        docs: false,
        blog: false,
        pages: false, // 기본 pages 끔
      },
    ],
  ],

  plugins: [
    // 🔗 Redirects
    [
      '@docusaurus/plugin-client-redirects',
      {
        redirects: [
          {
            from: '/download',
            to: 'https://github.com/treeentertainment/somecamera/releases/latest',
          },
        ],
      },
    ],

    // ===== ALL PAGES AT ONCE =====
    [
      '@docusaurus/plugin-content-pages',
      {
        path: 'pages',         // /pages 전체 등록!
        routeBasePath: '/',    // 루트 및 하위 경로에 노출
      },
    ],
   function webpackAliasPlugin() {
      return {
        name: 'webpack-alias-plugin',
        configureWebpack() {
          return {
            resolve: {
              alias: {
                '@site/src/pages': path.resolve('./pages'),
              },
              fallback: {
                path: require.resolve('path-browserify')
              }
            },
          };
        },
      };
    },
  ],

  themeConfig: {
    navbar: {
      title: 'SomeCamera',
      items: [
        // 필요한 경로만 남기세요
        { to: '/en/docs/intro', label: 'Docs (EN)', position: 'left' },
        { to: '/kr/docs/intro', label: 'Docs (KR)', position: 'left' },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        { label: 'GitHub', href: 'https://github.com/treeentertainment/SomeCamera' },
        { label: 'Docs (EN)', to: '/en/docs/intro' },
        { label: 'Docs (KR)', to: '/kr/docs/intro' },
      ],
    },
  },
};
