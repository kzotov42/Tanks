package com.ThirtyNineEighty.Game.Gameplay.Characteristics;

public class Characteristic
{
  public final String visualModelName;
  public final String phModelName;
  public final String textureName;

  private float health;
  private float speed; // m/s
  private float damage;
  private float rotationSpeed;

  public Characteristic(String visualModelName, String phModelName, String textureName)
  {
    this.visualModelName = visualModelName;
    this.phModelName = phModelName;
    this.textureName = textureName;
  }

  public Characteristic(Characteristic other)
  {
    visualModelName = other.visualModelName;
    phModelName = other.phModelName;
    textureName = other.textureName;

    health = other.getHealth();
    speed = other.getSpeed();
    damage = other.getDamage();
    rotationSpeed = other.getRotationSpeed();
  }

  public float getHealth() { return health; }
  public void setHealth(float value) { health = value; }

  public void subtractHealth(float value) { health -= value; }
  public void addHealth(float value) { health += value; }

  public float getSpeed() { return speed; }
  public void setSpeed(float value) { speed = value; }

  public float getDamage() { return damage; }
  public void setDamage(float value) { damage = value; }

  public float getRotationSpeed() { return rotationSpeed; }
  public void setRotationSpeed(float value) { rotationSpeed = value; }
}