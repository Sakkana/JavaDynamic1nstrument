# Jave Agent & Java ASM

## Appendix
Jenv 管理多版本 JDK

通过包管理工具中下载或从 IDEA 中下载

安装最新版本 openjdk
```zsh
brew install openjdk
```

安装 openjdk-17
```zsh
brew install openjdk@17
```

安装版本管理工具
```zsh
brew install jenv
```

添加多个 JAVA HOME 路径
```zsh
jenv add /Library/Java/JavaVirtualMachines/openjdk.jdk/Contents/Home
# 若从 IDEA 中安装则 jdk 存储于用户 Library 而非系统 Library 下
jenv add /Users/sakana/Library/Java/JavaVirtualMachines/corretto-17.0.13/Contents/Home    
```

启用配置文件
```zsh
vim ~/.zshrc
export PATH="$HOME/.jenv/bin:$PATH"
eval "$(jenv init -)"
source ~/.zshrc
```

项目局部指定 jdk 版本

在项目根路径下执行
```zsh
jenv local corretto64-17.0.13
```
之后项目目录下会出现一个 `.java-version`，之后只有在该目录下使用的是 local 版本的 jdk 17，在未显示指定的地方都是默认的 global jdk 23。
