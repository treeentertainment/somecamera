/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'SomeCamera',
  tagline: 'SomeCamera Documentation',
  url: 'https://camera.treeentertainment.tech',
  baseUrl: '/',
  favicon: 'favicon.ico',

  i18n: {
    defaultLocale: 'en',
    locales: ['kr', 'en'],
    localeConfigs: {
      kr: { label: '한국어' },
      en: { label: 'English' },
    },
  },

  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          path: 'docs',
          routeBasePath: 'docs',
          sidebarPath: ({ locale }) => {
            switch (locale) {
              case 'en':
              default:
                return require.resolve('./sidebars/sidebars_en.js');
              case 'kr':
                return require.resolve('./sidebars/sidebars_kr.js');
            }
          },
          editUrl:
            'https://github.com/treeentertainment/SomeCamera/edit/main/docs',
        },
      },
    ],
  ],

  themeConfig: {
    navbar: {
      title: 'SomeCamera',
      items: [
        { to: 'docs/intro', label: 'Docs', position: 'left' },
        { type: 'localeDropdown', position: 'right' },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        { label 'github', href: 'https://github.com/treeentertainment/somecamera' },
        { label: 'docs', to: 'docs/intro' },
      ],
    },
  },
};

module.exports = config;
