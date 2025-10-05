// @ts-check
// Docusaurus full config with KR/EN docs separated
import remarkDirective from 'remark-directive';
import remarkDirectiveRehype from 'remark-directive-rehype';

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'SomeCamera',
  tagline: 'SomeCamera Documentation',
  url: 'https://camera.treeentertainment.tech',
  baseUrl: '/',
  favicon: 'favicon.ico',
  onBrokenLinks: 'warn',
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
        docs: false,   // docs 플러그인 제거
        blog: false,
        pages: {
          path: 'pages', // 모든 문서를 pages 밑에서 관리
        },
      },
    ],
  ],
  themeConfig: {
    navbar: {
      title: 'SomeCamera',
      logo: {
        alt: 'SomeCamera',
        src: 'logo.png',
        href: '/intro', // title 클릭 시 /intro로 이동
      },
      items: [
        { to: '/intro', label: 'Intro', position: 'left' },
        { to: '/getting-started', label: 'Getting Started', position: 'left' },
        { to: '/guide/install', label: 'Guide', position: 'left' },
        { type: 'localeDropdown', position: 'right' },
        { href: 'https://github.com/treeentertainment/SomeCamera', label: 'GitHub', position: 'right' },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        { label: 'GitHub', href: 'https://github.com/treeentertainment/SomeCamera' },
      ],
    },
  },
};

export default config;
