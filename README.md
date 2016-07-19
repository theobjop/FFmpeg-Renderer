# FFmpeg-Renderer
An application which balances the rendering options for FFmpeg and renders it using an AVS script.
This makes rendering using FFmpeg easier for anyone. Comes complete with default settings which are the best for YouTube's upload.

## Information
I created this project because I was wasting a lot of time trying to figure out the x264 settings for FFmpeg.
The reason you should always be using FFmpeg is because it allows for more customization and faster encoding speeds.
Along with faster encoding speeds, you can change a lot of settings like adding a header called "faststart" which forces the file to
load faster on YouTube. This makes your videos spend less time processing after uploading and less time buffering when somebody watches it.

The amount of customization that FFmpeg allows is infinite. There are so many options that I created this program in order to change settings
on the fly so you can just open the program and run it.

### Windows Only
Unfortunately, the styling options for Java are mediocre and I am very opposed to other kinds of styling. The default styling options
for Java components is bulky, so I changed it to Windows Classic mode. This is probably why if you are on Windows in Aero mode it looks
"old." Ontop of that, the FFmpeg rendering options, the ones I use, as far as I know, are only compatible with Windows operating systems.

If you do decide to run this on any other operating system, please report back to me with information as to what happens. If it doesn't work,
if the style is off, or even if it works. I would love to change the settings to allow everyone to run it. Thanks!

## Features

- AVI Streaming files
- Fully customizable video and audio settings - TONS more to be added
- Progress bar from 1-100%
- One click solution
- Options saving
- Defaults for basic encoding

# Requirements

1. [Java 8](http://java.com/)
2. [FFmpeg](https://ffmpeg.org/)
3. [AviSynth](http://avisynth.nl/)

### Optional
  1. Rendering Software
    1. Adobe Premiere
    2. Sony Vegas
  2. [DebugMode's FrameServer](http://www.debugmode.com/frameserver/)


# How To Use
Download requirements if you haven't already, then download the program [jar](https://github.com/theobjop/FFmpeg-Renderer/blob/master/FFmpeg%20Renderer.jar).
Run the Runnable JAR file, FFmpeg Renderer. Browse for the executable FFmpeg and AVI file or directory. Change the settings to your liking, then, when ready, click the Render button in the bottom right.

## Known (Possible) Issues
- JAR file might not run if you don't have the dependencies
- Opt.ini might not be created if you don't run as administrator

Please message me if you get any of these errors, especially if the dependencies aren't there because I will have to recompile differently.
