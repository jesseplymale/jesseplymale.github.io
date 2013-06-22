---
layout: post
title:  Maschine Step Size and Pattern Length
tags: music
categories: jesseplymale
---

One of the things I have found confusing about learning Maschine is the 
correspondence between step size and pattern length. To muddle things further, 
there’s two “Pattern Length” parameters: One for the pattern, and one for the 
grid.

After much reading of manuals, here’s my synopsis:

*   The “Grid Pattern Length” (which you access by pushing the “Grid” button
    and then the top button #3 for “Pat Len”) is the unit of measure which your
    pattern length will be measured by. If you choose “1 Bar” here (the default),
    you will later be able to make your pattern 1 bar long, 2 bars long, etc. If
    you choose “1/2”  here (which really means for “1/2 Bar”), you will be able to
    make your pattern 1 of these units long (which would be a 1/2 bar pattern),
    or 2 (which would be a 1 bar pattern), etc.

    *   One way you can see this working is to start out with your pattern as a
        1 bar length (the default). Then change your grid pattern length to 1/2 bar. Go
        back to your pattern length and you will see that the pattern length is now 2
        instead of 1. That’s because your pattern is still the same size (1 bar), but
        its unit of measure is now in half bars instead of full bars.

    *   The “Pattern Length” (which you access by pushing the pattern button and
        twisting knob #1) is the length of the pattern, given in units of “Grid Pattern
        Length” (as discussed above)

*   The Step Size (which you access by pushing the “Grid” button and then the
    top button #4 for “Step”) is used for two different things:

    *   The step size for step mode. This influences how many of the pads on
        the controller will be needed to cover the whole pattern. If you set the step
        size to 1/4 (corresponding to 1/4 of a bar—in other words, a quarter note) and
        your pattern is 1 bar long, only 4 lights will sequentially light up when you
        push play in step mode. If you set the step size to 1/16 and the pattern is one
        bar long (the default), all 16 pads will light up in sequence as the pattern
        plays.

    *   The step size is also the unit of measure for quantization. If you do a
        quantize on your pattern and the step size is set to 1/8, all the notes will
        snap to the 1/8 bar markings.

<img class="centeredImage" src="/media/svg/maschine.svg" alt="Maschine Step Size and Pattern Length Diagram"/>

Hopefully this little explanation will be helpful to somebody out there who is 
also going through the Maschine learning process.
