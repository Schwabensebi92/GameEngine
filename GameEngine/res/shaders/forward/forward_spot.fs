#version 330

in vec2 textureCoordinate0;
in vec3 normal0;
in vec3 worldPosition0;

out vec4 fragColor;

struct BaseLight
{
    vec3 color;
    float intensity;
};

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    BaseLight baseLight;
    Attenuation attenuation;
    vec3 position;
    float range;
};

struct SpotLight
{
    PointLight pointLight;
    vec3 direction;
    float cutoff;
};

uniform vec3 eyePosition;

uniform sampler2D sampler;

uniform float specularIntensity;
uniform float specularExponent;

uniform SpotLight spotLight;

vec4 calculateLight(BaseLight baseLight, vec3 direction, vec3 normal)
{
    float diffuseFactor = dot(normal, -direction);
    
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specularColor = vec4(0, 0, 0, 0);
    
    if (diffuseFactor > 0)
    {
        diffuseColor = vec4(baseLight.color, 1.0) * baseLight.intensity * diffuseFactor;
        
        vec3 directionToEye = normalize(eyePosition - worldPosition0);
        vec3 reflectDirection = normalize(reflect(direction, normal));
        
        float specularFactor = dot(directionToEye, reflectDirection);
        specularFactor = pow(specularFactor, specularExponent);
        
        if (specularFactor > 0)
        {
            specularColor = vec4(baseLight.color, 1.0) * specularIntensity * specularFactor;
        }
    }
    
    return diffuseColor + specularColor;
}

vec4 calculatePointLight(PointLight pointLight, vec3 normal)
{
    vec3 lightDirection = worldPosition0 - pointLight.position;
    float distanceToPoint = length(lightDirection);
    
    if (distanceToPoint > pointLight.range)
    {
        return vec4(0, 0, 0, 0);
    }
    
    lightDirection = normalize(lightDirection);
    
    vec4 color = calculateLight(pointLight.baseLight, lightDirection, normal);
    
    float attenuation = pointLight.attenuation.constant +
    pointLight.attenuation.linear * distanceToPoint +
    pointLight.attenuation.exponent * distanceToPoint * distanceToPoint;
    
    if (attenuation <= 0)
    {
        attenuation = 0.0001;
    }
    
    return color / attenuation;
}

vec4 calculateSpotLight(SpotLight spotLight, vec3 normal)
{
    vec3 lightDirection = normalize(worldPosition0 - spotLight.pointLight.position);
    float spotFactor = dot(lightDirection, spotLight.direction);
    
    vec4 color = vec4(0, 0, 0, 0);
    
    if (spotFactor > spotLight.cutoff)
    {
        color = calculatePointLight(spotLight.pointLight, normal) * (1.0 - (1.0 - spotFactor) / (1.0 - spotLight.cutoff));
    }
    
    return color;
}

void main()
{
    fragColor = texture(sampler, textureCoordinate0.xy) * calculateSpotLight(spotLight, normalize(normal0));;
}