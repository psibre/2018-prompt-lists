# Various prompt lists, in various formats

Create a markdown file like this:
```markdown
# Prompts

## foo
<audio data-autoplay src='beep.mp3'></audio>
Foo.

## bar
<audio data-autoplay src='beep.mp3'></audio>
Bar.
```
Add a header like this:
```yaml
revealjs-url: https://revealjs.com/
header-includes: |
    \usepackage{media9}
    \setbeamertemplate{background canvas}{%
      \includemedia[addresource=beep.mp3, width=1em, height=1ex, flashvars={%
        source=beep.mp3&autoPlay=true&hideBar=true
      }]{}{APlayer.swf}
    }
```
Process it with the `compileBeamerPrompts` and `compileRevealJsPrompts` tasks.

Pandoc must be installed.
