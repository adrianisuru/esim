#version 330 core

uniform mat4 pm;
uniform mat4 wm;

layout (location=0) in vec3 in_Position;
//layout (location=1) in vec3 in_Color;

out vec4 pass_Color;

void main()
{
	gl_Position = pm * wm * vec4(in_Position, 1.0);
	//gl_Position = vec4(0,0,0,0);
	pass_Color = vec4(1.0, 1.0, 1.0, 1.0);
}
