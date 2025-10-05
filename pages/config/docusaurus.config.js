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

  presets: [
    [
      'classic',
      {
        docs: false, // docs preset 안 쓰고, plugin-content-docs 로 따로 등록
      },
    ],
  ],

  plugins: [
    // 🔗 리다이렉트
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

    // 📚 English docs
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

    // 📚 Korean docs
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

    // ⚙️ Webpack alias (v3에서는 plugin으로 등록해야 함)
    function customWebpackPlugin() {
      return {
        name: 'custom-webpack-plugin',
        configureWebpack() {
          return {
            resolve: {
              alias: {
                '@site/src/pages': path.resolve('./pages'),
              },
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
        { to: '/docs/en/intro', label: 'Docs (EN)', position: 'left' },
        { to: '/docs/kr/intro', label: 'Docs (KR)', position: 'left' },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        { label: 'GitHub', href: 'https://github.com/treeentertainment/SomeCamera' },
        { label: 'Docs (EN)', to: '/docs/en/intro' },
        { label: 'Docs (KR)', to: '/docs/kr/intro' },
      ],
    },
  },
};
