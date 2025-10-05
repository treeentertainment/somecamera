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
      onBrokenMarkdownLinks: 'warn', // ✅ v4 대응
    },
  },

  themes: ['@docusaurus/theme-mermaid'],

  presets: [
    [
      'classic',
      {
        docs: false,
        blog: false,
        pages: false, // ✅ 기본 pages 끔 (중복 방지)
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

    // ===== ENGLISH =====
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'en-docs',
        path: 'pages/en/docs',                 // ✅ /pages/en/docs
        routeBasePath: 'en/docs',
        sidebarPath: path.resolve('./pages/config/sidebars/sidebars_en.js'),
        remarkPlugins: [remarkGithubAdmonitionsToDirectives],
      },
    ],
    [
      '@docusaurus/plugin-content-pages',
      {
        id: 'en-intro',
        path: 'pages/en/intro',                // ✅ /pages/en/intro
        routeBasePath: 'en/intro',
      },
    ],

    // ===== KOREAN =====
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'kr-docs',
        path: 'pages/kr/docs',                 // ✅ /pages/kr/docs
        routeBasePath: 'kr/docs',
        sidebarPath: path.resolve('./pages/config/sidebars/sidebars_kr.js'),
        remarkPlugins: [remarkGithubAdmonitionsToDirectives],
      },
    ],
    [
      '@docusaurus/plugin-content-pages',
      {
        id: 'kr-intro',
        path: 'pages/kr/intro',                // ✅ /pages/kr/intro
        routeBasePath: 'kr/intro',
      },
    ],
    // ⚙️ Webpack alias
    function webpackAliasPlugin() {
      return {
        name: 'webpack-alias-plugin',
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
        { to: '/en/intro', label: 'Intro (EN)', position: 'left' },
        { to: '/en/docs/intro', label: 'Docs (EN)', position: 'left' },
        { to: '/kr/intro', label: 'Intro (KR)', position: 'left' },
        { to: '/kr/docs/intro', label: 'Docs (KR)', position: 'left' },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        { label: 'GitHub', href: 'https://github.com/treeentertainment/SomeCamera' },
        { label: 'Intro (EN)', to: '/en/intro' },
        { label: 'Docs (EN)', to: '/en/docs/intro' },
        { label: 'Intro (KR)', to: '/kr/intro' },
        { label: 'Docs (KR)', to: '/kr/docs/intro' },
      ],
    },
  },
};
