export interface Contact {
  name: string
  url: string
  icon: string
}

export interface AuthorInfo {
  avatarUrl: string
  authorName: string
  name: string
  motto: string
  title: string
  description: string
  githubUrl: string
  githubIcon: string
  email: string
  emailIcon: string
  contacts: Contact[]
}

const authorInfo: AuthorInfo = {
  avatarUrl: '/avatar.jpg',
  authorName: 'tgapk',
  name: 'tgapk',
  motto: '做自己的英雄',
  title: '全栈开发工程师',
  description: '做自己的英雄',
  githubUrl: 'https://github.com/tg-tian/Blog',
  githubIcon: '/github.svg',
  email: '914837973@qq.com',
  emailIcon: '/email.svg',
  contacts: [
    {
      name: 'GitHub',
      url: 'https://github.com/tg-tian/Blog',
      icon: '/github.svg'
    },
    {
      name: 'Email',
      url: 'mailto:914837973@qq.com',
      icon: '/email.svg'
    }
  ]
}

export default authorInfo

