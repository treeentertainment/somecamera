
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
  plugins: [
        [
      '@docusaurus/plugin-sitemap',
      {
        id: 'sitemap-en',
        changefreq: 'weekly',
        priority: 0.5,
      }
    ],
    [
      '@docusaurus/plugin-sitemap',
      {
        id: 'sitemap-kr',
        changefreq: 'weekly',
        priority: 0.5,
      }
    ],
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
     [
      '@docusaurus/plugin-content-pages',
      {
        path: 'pages',
        routeBasePath: '/',
        include: ['index.md'], // Only include pages/index.md
      },
    ],
    [
    '@docusaurus/plugin-content-docs',
    {
      id: 'en',
      path: './pages/en',
      routeBasePath: 'en',
      sidebarPath: require.resolve('./sidebars/sidebars_en.js'),
      },
    ],
    [
    '@docusaurus/plugin-content-docs',
    {
      id: 'kr',
      path: './pages/kr',
      routeBasePath: 'kr',
      sidebarPath: require.resolve('./sidebars/sidebars_kr.js'),
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
                path: require.resolve('path-browserify'),
              },
            },
          };
        },
      };
    },
    // ⚡ 커스텀 테마 plugin
    function ThemePlugin() {
      return {
        name: 'theme-plugin',
        getThemePath() {
          return path.resolve(__dirname, './pages/config/theme');
        },
      };
    },
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
