export default {
  title: 'SomeCamera',
  url: 'https://camera.treeentertainment.tech',
  baseUrl: '/',
  favicon: 'favicon.ico',

  onBrokenLinks: 'warn',
  onBrokenMarkdownLinks: 'warn',

  i18n: {
    defaultLocale: 'en',
    locales: ['en'], // i18n 기능 대신 직접 /docs/en, /docs/kr 관리
  },

  presets: [
    [
      'classic',
      {
       docs: false,
       markdown: {
         beforeDefaultRemarkPlugins: [remarkGithubAdmonitionsToDirectives],
       },
      },
    ],
  ],

  plugins: [
    [
      '@docusaurus/plugin-client-redirects',
      {
        redirects: [{ from: '/download', to: 'https://github.com/treeentertainment/somecamera/releases/latest' }],
      },
    ],
    [
    '@docusaurus/plugin-content-docs',
    {
      id: 'en',
      path: 'pages/docs/en',
      routeBasePath: 'docs/en',
      sidebarPath: require.resolve('./pages/config/sidebars/sidebars_en.js'),
    },
    ],
    [
    '@docusaurus/plugin-content-docs',
    {
      id: 'kr',
      path: 'pages/docs/kr',
      routeBasePath: 'docs/kr',
      sidebarPath: require.resolve('./pages/config/sidebars/sidebars_kr.js'),
    },
    ],
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
