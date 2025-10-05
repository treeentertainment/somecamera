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
    locales: ['en'],
  },

  themes: ['@docusaurus/theme-mermaid'],
  presets: [
    [
      'classic',
      {
        docs: false, // docs preset 자체는 안 쓰고, plugin-content-docs 로 따로 등록
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
        sidebarPath: require.resolve('./sidebars/sidebars_en.js'),
        remarkPlugins: [remarkGithubAdmonitionsToDirectives],
      },
    ],
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'kr',
        path: 'pages/docs/kr',
        routeBasePath: 'docs/kr',
        sidebarPath: require.resolve('./sidebars/sidebars_kr.js'),
        remarkPlugins: [remarkGithubAdmonitionsToDirectives],
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
