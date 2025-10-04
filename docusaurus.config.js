// @ts-check
// Docusaurus full config with KR/EN docs separated

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'SomeCamera',
  tagline: 'SomeCamera Documentation',
  url: 'https://camera.treeentertainment.tech',
  baseUrl: '/',
  favicon: 'favicon.ico',

  // i18n 기본 설정
  i18n: {
    defaultLocale: 'en',
    locales: ['en', 'kr'],
    localeConfigs: {
      en: { label: 'English' },
      kr: { label: '한국어' },
    },
  },

  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: false, // 기본 docs 비활성화 (우리가 plugin으로 따로 넣음)
      },
    ],
  ],

  plugins: [
    // Korean docs
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'kr',
        path: 'docs/kr',
        routeBasePath: 'docs/kr',
        sidebarPath: require.resolve('./sidebars/sidebars_kr.js'),
        editUrl: 'https://github.com/treeentertainment/SomeCamera/tree/main/docs/kr',
      },
    ],
    // English docs
    [
      '@docusaurus/plugin-content-docs',
      {
        id: 'en',
        path: 'docs/en',
        routeBasePath: 'docs/en',
        sidebarPath: require.resolve('./sidebars/sidebars_en.js'),
        editUrl: 'https://github.com/treeentertainment/SomeCamera/tree/main/docs/en',
      },
    ],
  ],

  themeConfig: {
    navbar: {
      title: 'SomeCamera',
      items: [
        { to: '/docs/en/intro/', label: 'Docs (EN)', position: 'left' },
        { to: '/docs/kr/intro/', label: 'Docs (KR)', position: 'left' },
        { type: 'localeDropdown', position: 'right' },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          label: 'GitHub',
          href: 'https://github.com/treeentertainment/SomeCamera',
        },
        {
          label: 'Docs (EN)',
          to: '/docs/en/intro/',
        },
        {
          label: 'Docs (KR)',
          to: '/docs/kr/intro/',
        },
      ],
    },
  },
};

module.exports = config;
