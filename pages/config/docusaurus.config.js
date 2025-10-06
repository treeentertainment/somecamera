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
        docs: {
          routeBasePath: '/', // docs를 루트에서 노출
          sidebarPath: false, // 🔑 함수 대신 false (여기는 루트용 docs는 안 씀)
        },
        blog: false,
        pages: false,
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
            from: ['/download', '/en/download', '/kr/download'],
            to: 'https://github.com/treeentertainment/somecamera/releases/latest',
          },
          {
            from: ['/github', '/en/github', '/kr/github'],
            to: 'https://github.com/treeentertainment/somecamera',
          },
        ],
      },
    ],

    // Pages plugin
    [
      '@docusaurus/plugin-content-pages',
      {
        path: 'pages',
        routeBasePath: '/',
        exclude: ['**/config/**'],
      },
    ],

    // Webpack alias
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
                path: require.resolve('path-browserify'),
              },
            },
          };
        },
      };
    },

    // EN Docs
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'docs-en',
        path: 'pages/en/docs',
        routeBasePath: 'en/docs',
        sidebarPath: require.resolve('./sidebars/sidebars_en.js'),
      },
    ],
    // EN Intro
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'intro-en',
        path: 'pages/en/intro',
        routeBasePath: 'en/intro',
        sidebarPath: require.resolve('./sidebars/sidebars_en.js'),
      },
    ],

    // KR Docs
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'docs-kr',
        path: 'pages/kr/docs',
        routeBasePath: 'kr/docs',
        sidebarPath: require.resolve('./sidebars/sidebars_kr.js'),
      },
    ],
    // KR Intro
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'intro-kr',
        path: 'pages/kr/intro',
        routeBasePath: 'kr/intro',
        sidebarPath: require.resolve('./sidebars/sidebars_kr.js'),
      },
    ],
  ],

  themeConfig: {
    navbar: {
      title: 'SomeCamera',
      items: [
        { href: '/github/', label: 'GitHub', position: 'right', target: '_blank' },
        { href: '/download/', label: 'Download', position: 'right', target: '_blank' },
        { to: '/en/intro', label: 'Docs (EN)', position: 'left' },
        { to: '/kr/intro', label: 'Docs (KR)', position: 'left' },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        { label: 'GitHub', href: '/github/', target: '_blank' },
        { label: 'Download', href: '/download/', target: '_blank' },
        { label: 'Docs (EN)', to: '/en/intro' },
        { label: 'Docs (KR)', to: '/kr/intro' },
      ],
    },
  },
};
